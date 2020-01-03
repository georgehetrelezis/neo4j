/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.harness;

import java.io.File;
import java.util.function.Function;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.config.Setting;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.UserAggregationFunction;
import org.neo4j.procedure.UserFunction;

/**
 * Utility for constructing and starting Neo4j for test purposes.
 */
public interface TestServerBuilder
{
    /**
     * Start a new server. By default, the server will listen to a random free port, and you can determine where to
     * connect using the {@link ServerControls#httpURI()} method. You could also specify explicit ports using the
     * {@link #withConfig(org.neo4j.graphdb.config.Setting, String)} method. Please refer to the Neo4j Manual for
     * details on available configuration options.
     *
     * When the returned controls are {@link ServerControls#close() closed}, the temporary directory the server used
     * will be removed as well.
     */
    ServerControls newServer();

    /**
     * Configure the Neo4j instance. Configuration here can be both configuration aimed at the server as well as the
     * database tuning options. Please refer to the Neo4j Manual for details on available configuration options.
     *
     * @param key the config key
     * @param value the config value
     * @return this builder instance
     */
    TestServerBuilder withConfig( Setting<?> key, String value );

    /**
     * @see #withConfig(org.neo4j.graphdb.config.Setting, String)
     */
    TestServerBuilder withConfig( String key, String value );

    /**
     * Shortcut for configuring the server to use an unmanaged extension. Please refer to the Neo4j Manual on how to
     * write unmanaged extensions.
     *
     * @param mountPath the http path, relative to the server base URI, that this extension should be mounted at.
     * @param extension the extension class.
     * @return this builder instance
     */
    TestServerBuilder withExtension( String mountPath, Class<?> extension );

    /**
     * Shortcut for configuring the server to find and mount all unmanaged extensions in the given package.
     * @see #withExtension(String, Class)
     * @param mountPath the http path, relative to the server base URI, that this extension should be mounted at.
     * @param packageName a java package with extension classes.
     * @return this builder instance
     */
    TestServerBuilder withExtension( String mountPath, String packageName );

    /**
     * Data fixtures to inject upon server start. This can be either a file with a plain-text cypher query
     * (for example, myFixture.cyp), or a directory containing such files with the suffix ".cyp".
     * @param cypherFileOrDirectory file with cypher statement, or directory containing ".cyp"-suffixed files.
     * @return this builder instance
     */
    TestServerBuilder withFixture( File cypherFileOrDirectory );

    /**
     * Data fixture to inject upon server start. This should be a valid Cypher statement.
     * @param fixtureStatement a cypher statement
     * @return this builder instance
     */
    TestServerBuilder withFixture( String fixtureStatement );

    /**
     * Data fixture to inject upon server start. This should be a user implemented fixture function
     * operating on a {@link GraphDatabaseService} instance
     * @param fixtureFunction a fixture function
     * @return this builder instance
     */
    TestServerBuilder withFixture( Function<GraphDatabaseService, Void> fixtureFunction );

    /**
     * Pre-populate the server with databases copied from the specified source directory.
     * The source directory needs to have sub-folders `databases/graph.db` in which the source store files are located.
     * @param sourceDirectory the directory to copy from
     * @return this builder instance
     */
    TestServerBuilder copyFrom( File sourceDirectory );

    /**
     * Configure the server to load the specified procedure definition class. The class should contain one or more
     * methods annotated with {@link Procedure}, these will become available to call through
     * cypher.
     *
     * @param procedureClass a class containing one or more procedure definitions
     * @return this builder instance
     */
    TestServerBuilder withProcedure( Class<?> procedureClass );

    /**
     * Configure the server to load the specified function definition class. The class should contain one or more
     * methods annotated with {@link UserFunction}, these will become available to call through
     * cypher.
     *
     * @param functionClass a class containing one or more function definitions
     * @return this builder instance
     */
    TestServerBuilder withFunction( Class<?> functionClass );

    /**
     * Configure the server to load the specified aggregation function definition class. The class should contain one or more
     * methods annotated with {@link UserAggregationFunction}, these will become available to call through
     * cypher.
     *
     * @param functionClass a class containing one or more function definitions
     * @return this builder instance
     */
    TestServerBuilder withAggregationFunction( Class<?> functionClass );
}
