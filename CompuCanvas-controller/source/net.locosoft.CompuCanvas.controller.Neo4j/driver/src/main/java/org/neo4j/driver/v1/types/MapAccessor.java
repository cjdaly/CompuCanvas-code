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
package org.neo4j.driver.v1.types;

import java.util.Map;

import org.neo4j.driver.internal.value.NullValue;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.neo4j.driver.v1.util.Function;

/**
 * Access the keys, properties and values of an underlying unordered map by key
 *
 * This provides only read methods. Subclasses may chose to provide additional methods
 * for changing the underlying map.
 * @since 1.0
 */
public interface MapAccessor
{
    /**
     * Retrieve the keys of the underlying map
     *
     * @return all map keys in unspecified order
     */
    Iterable<String> keys();

    /**
     * Check if the list of keys contains the given key
     *
     * @param key the key
     * @return <tt>true</tt> if this map keys contains the given key otherwise <tt>false</tt>
     */
    boolean containsKey( String key );

    /**
     * Retrieve the value of the property with the given key
     *
     * @param key the key of the property
     * @return the property's value or a {@link NullValue} if no such key exists
     * @throws ClientException if record has not been initialized
     */
    Value get( String key );

    /**
     * Retrieve the number of entries in this map
     *
     * @return the number of entries in this map
     */
    int size();

    /**
     * Retrieve all values of the underlying collection
     *
     * @return all values in unspecified order
     */
    Iterable<Value> values();

    /**
     * Map and retrieve all values of the underlying collection
     *
     * @param mapFunction a function to map from Value to T. See {@link Values} for some predefined functions, such
     * as {@link Values#ofBoolean()}, {@link Values#ofList(Function)}.
     * @param <T> the target type of mapping
     * @return the result of mapping all values in unspecified order
     */
    <T> Iterable<T> values( Function<Value, T> mapFunction );

    /**
     * Return the underlying map as a map of string keys and values converted using
     * {@link Value#asObject()}.
     *
     * This is equivalent to calling {@link #asMap(Function)} with {@link Values#ofObject()}.
     *
     * @return the value as a Java map
     */
    Map<String, Object> asMap();

    /**
     * @param mapFunction a function to map from Value to T. See {@link Values} for some predefined functions, such
     * as {@link Values#ofBoolean()}, {@link Values#ofList(Function)}.
     * @param <T> the type of map values
     * @see Values for a long list of built-in conversion functions
     * @return the value as a map from string keys to values of type T obtained from mapping he original map values, if possible
     */
    <T> Map<String, T> asMap( Function<Value, T> mapFunction );
}
