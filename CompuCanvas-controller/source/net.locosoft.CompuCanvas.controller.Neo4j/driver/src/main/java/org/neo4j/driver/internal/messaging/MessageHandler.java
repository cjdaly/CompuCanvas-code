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
package org.neo4j.driver.internal.messaging;

import java.io.IOException;
import java.util.Map;

import org.neo4j.driver.v1.Value;

public interface MessageHandler
{
    // Requests
    void handleInitMessage( String clientNameAndVersion, Map<String,Value> authToken ) throws IOException;

    void handleRunMessage( String statement, Map<String,Value> parameters ) throws IOException;

    void handlePullAllMessage() throws IOException;

    void handleDiscardAllMessage() throws IOException;

    void handleResetMessage() throws IOException;

    void handleAckFailureMessage() throws IOException;

    // Responses
    void handleSuccessMessage( Map<String,Value> meta ) throws IOException;

    void handleRecordMessage( Value[] fields ) throws IOException;

    void handleFailureMessage( String code, String message ) throws IOException;

    void handleIgnoredMessage() throws IOException;

}
