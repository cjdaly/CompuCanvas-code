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
package org.neo4j.driver.internal.logging;

import org.neo4j.driver.v1.Logger;
import org.neo4j.driver.v1.Logging;

public class DevNullLogging implements Logging
{
    public static final Logging DEV_NULL_LOGGING = new DevNullLogging();

    private DevNullLogging()
    {
    }

    @Override
    public Logger getLog( String name )
    {
        return DevNullLogger.DEV_NULL_LOGGER;
    }
}
