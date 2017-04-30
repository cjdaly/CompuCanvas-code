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
package org.neo4j.driver.internal;

import org.neo4j.driver.internal.net.BoltServerAddress;
import org.neo4j.driver.internal.spi.ConnectionPool;
import org.neo4j.driver.internal.spi.ConnectionProvider;
import org.neo4j.driver.internal.spi.PooledConnection;
import org.neo4j.driver.v1.AccessMode;

/**
 * Simple {@link ConnectionProvider connection provider} that obtains connections form the given pool only for
 * the given address.
 */
public class DirectConnectionProvider implements ConnectionProvider
{
    private final BoltServerAddress address;
    private final ConnectionPool pool;

    DirectConnectionProvider( BoltServerAddress address, ConnectionPool pool )
    {
        this.address = address;
        this.pool = pool;
    }

    @Override
    public PooledConnection acquireConnection( AccessMode mode )
    {
        return pool.acquire( address );
    }

    @Override
    public void close() throws Exception
    {
        pool.close();
    }

    public BoltServerAddress getAddress()
    {
        return address;
    }
}
