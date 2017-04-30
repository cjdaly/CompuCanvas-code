/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.driver.internal.net;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.neo4j.driver.internal.logging.DelegatingLogger;
import org.neo4j.driver.internal.messaging.InitMessage;
import org.neo4j.driver.internal.messaging.Message;
import org.neo4j.driver.internal.messaging.RunMessage;
import org.neo4j.driver.internal.security.SecurityPlan;
import org.neo4j.driver.internal.spi.Collector;
import org.neo4j.driver.internal.spi.Connection;
import org.neo4j.driver.internal.summary.InternalServerInfo;
import org.neo4j.driver.v1.Logger;
import org.neo4j.driver.v1.Logging;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.neo4j.driver.v1.exceptions.Neo4jException;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;
import org.neo4j.driver.v1.summary.ServerInfo;

import static org.neo4j.driver.internal.messaging.AckFailureMessage.ACK_FAILURE;
import static org.neo4j.driver.internal.messaging.DiscardAllMessage.DISCARD_ALL;
import static org.neo4j.driver.internal.messaging.PullAllMessage.PULL_ALL;
import static org.neo4j.driver.internal.messaging.ResetMessage.RESET;

public class SocketConnection implements Connection
{
    private static final String LOG_NAME = "Connection";

    private final Queue<Message> pendingMessages = new LinkedList<>();
    private final SocketResponseHandler responseHandler;
    private final AtomicBoolean isInterrupted = new AtomicBoolean( false );
    private final AtomicBoolean isAckFailureMuted = new AtomicBoolean( false );
    private InternalServerInfo serverInfo;

    private final SocketClient socket;

    SocketConnection( BoltServerAddress address, SecurityPlan securityPlan, int timeoutMillis, Logging logging )
    {
        Logger logger = new DelegatingLogger( logging.getLog( LOG_NAME ), String.valueOf( hashCode() ) );
        this.socket = new SocketClient( address, securityPlan, timeoutMillis, logger );
        this.responseHandler = createResponseHandler( logger );

        startSocketClient();
    }

    /**
     * Create new connection backed by the given socket.
     * <p>
     * <b>Note:</b> this constructor should be used <b>only</b> for testing.
     *
     * @param socket the socket to use for network interactions.
     * @param serverInfo the info about server this connection points to.
     * @param logger the logger.
     */
    public SocketConnection( SocketClient socket, InternalServerInfo serverInfo, Logger logger )
    {
        this.socket = socket;
        this.serverInfo = serverInfo;
        this.responseHandler = createResponseHandler( logger );

        startSocketClient();
    }

    private void startSocketClient()
    {
        try
        {
            this.socket.start();
        }
        catch ( Throwable e )
        {
            close();
            throw e;
        }
    }

    private SocketResponseHandler createResponseHandler( Logger logger )
    {
        if( logger.isDebugEnabled() )
        {
            return new LoggingResponseHandler( logger );
        }
        else
        {
            return new SocketResponseHandler();
        }
    }

    @Override
    public void init( String clientName, Map<String,Value> authToken )
    {
        Collector.InitCollector initCollector = new Collector.InitCollector();
        queueMessage( new InitMessage( clientName, authToken ), initCollector );
        sync();
        this.serverInfo = new InternalServerInfo( socket.address(), initCollector.serverVersion() );
    }

    @Override
    public void run( String statement, Map<String,Value> parameters, Collector collector )
    {
        queueMessage( new RunMessage( statement, parameters ), collector );
    }

    @Override
    public void discardAll( Collector collector )
    {
        queueMessage( DISCARD_ALL, collector );
    }

    @Override
    public void pullAll( Collector collector )
    {
        queueMessage( PULL_ALL, collector );
    }

    @Override
    public void reset()
    {
        queueMessage( RESET, Collector.RESET );
    }

    @Override
    public void ackFailure()
    {
        queueMessage( ACK_FAILURE, Collector.ACK_FAILURE );
    }

    @Override
    public void sync()
    {
        flush();
        receiveAll();
    }

    @Override
    public synchronized void flush()
    {
        ensureNotInterrupted();

        try
        {
            socket.send( pendingMessages );
        }
        catch ( IOException e )
        {
            throw new ServiceUnavailableException( "Unable to send messages to server: " + e.getMessage(), e );
        }
    }

    private void ensureNotInterrupted()
    {
        try
        {
            if( isInterrupted.get() )
            {
                // receive each of it and throw error immediately
                while ( responseHandler.collectorsWaiting() > 0 )
                {
                    receiveOne();
                }
            }
        }
        catch ( Neo4jException e )
        {
            throw new ClientException(
                    "An error has occurred due to the cancellation of executing a previous statement. " +
                    "You received this error probably because you did not consume the result immediately after " +
                    "running the statement which get reset in this session.", e );
        }

    }

    private void receiveAll()
    {
        try
        {
            socket.receiveAll( responseHandler );
            assertNoServerFailure();
        }
        catch ( IOException e )
        {
            throw mapRecieveError( e );
        }
    }

    @Override
    public void receiveOne()
    {
        try
        {
            socket.receiveOne( responseHandler );
            assertNoServerFailure();
        }
        catch ( IOException e )
        {
            throw mapRecieveError( e );
        }
    }

    private void assertNoServerFailure()
    {
        if ( responseHandler.serverFailureOccurred() )
        {
            Neo4jException exception = responseHandler.serverFailure();
            responseHandler.clearError();
            isInterrupted.set( false );
            throw exception;
        }
    }

    private ClientException mapRecieveError( IOException e )
    {
        String message = e.getMessage();
        if ( message == null )
        {
            return new ClientException( "Unable to read response from server: " + e.getClass().getSimpleName(), e );
        }
        else if ( e instanceof SocketTimeoutException )
        {
            return new ClientException( "Server did not reply within the network timeout limit.", e );
        }
        else
        {
            return new ClientException( "Unable to read response from server: " + message, e );
        }
    }

    private synchronized void queueMessage( Message msg, Collector collector )
    {
        ensureNotInterrupted();

        pendingMessages.add( msg );
        responseHandler.appendResultCollector( collector );
    }

    @Override
    public void close()
    {
        socket.stop();
    }

    @Override
    public boolean isOpen()
    {
        return socket.isOpen();
    }

    @Override
    public synchronized void resetAsync()
    {
        queueMessage( RESET, new Collector.ResetCollector( new Runnable()
        {
            @Override
            public void run()
            {
                isInterrupted.set( false );
                isAckFailureMuted.set( false );
            }
        } ) );
        flush();
        isInterrupted.set( true );
        isAckFailureMuted.set( true );
    }

    @Override
    public boolean isAckFailureMuted()
    {
        return isAckFailureMuted.get();
    }

    @Override
    public ServerInfo server()
    {
        return this.serverInfo;
    }

    @Override
    public BoltServerAddress boltServerAddress()
    {
        return this.serverInfo.boltServerAddress();
    }
}
