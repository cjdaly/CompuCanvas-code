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
package org.neo4j.driver.v1.summary;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.util.Immutable;

/**
 * The result summary of running a statement. The result summary interface can be used to investigate
 * details about the result, like the type of query run, how many and which kinds of updates have been executed,
 * and query plan and profiling information if available.
 *
 * The result summary is only available after all result records have been consumed.
 *
 * Keeping the result summary around does not influence the lifecycle of any associated session and/or transaction.
 * @since 1.0
 */
@Immutable
public interface ResultSummary
{
    /**
     * @return statement that has been executed
     */
    Statement statement();

    /**
     * @return counters for operations the statement triggered
     */
    SummaryCounters counters();

    /**
     * @return type of statement that has been executed
     */
    StatementType statementType();

    /**
     * @return true if the result contained a statement plan, i.e. is the summary of a Cypher "PROFILE" or "EXPLAIN" statement
     */
    boolean hasPlan();

    /**
     * @return true if the result contained profiling information, i.e. is the summary of a Cypher "PROFILE" statement
     */
    boolean hasProfile();

    /**
     * This describes how the database will execute your statement.
     *
     * @return statement plan for the executed statement if available, otherwise null
     */
    Plan plan();

    /**
     * This describes how the database did execute your statement.
     *
     * If the statement you executed {@link #hasProfile() was profiled}, the statement plan will contain detailed
     * information about what each step of the plan did. That more in-depth version of the statement plan becomes
     * available here.
     *
     * @return profiled statement plan for the executed statement if available, otherwise null
     */
    ProfiledPlan profile();

    /**
     * A list of notifications that might arise when executing the statement.
     * Notifications can be warnings about problematic statements or other valuable information that can be presented
     * in a client.
     *
     * Unlike failures or errors, notifications do not affect the execution of a statement.
     *
     * @return a list of notifications produced while executing the statement. The list will be empty if no
     * notifications produced while executing the statement.
     */
    List<Notification> notifications();

    /**
     * The time it took the server to make the result available for consumption.
     *
     * @param unit The unit of the duration.
     * @return The time it took for the server to have the result available in the provided time unit.
     */
    long resultAvailableAfter( TimeUnit unit );

    /**
     * The time it took the server to consume the result.
     *
     * @param unit The unit of the duration.
     * @return The time it took for the server to consume the result in the provided time unit.
     */
    long resultConsumedAfter( TimeUnit unit );

    /**
     * The basic information of the server where the result is obtained from
     * @return basic information of the server where the result is obtain from
     */
    ServerInfo server();
}
