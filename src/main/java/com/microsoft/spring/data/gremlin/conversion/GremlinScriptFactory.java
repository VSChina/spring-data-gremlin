/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

public abstract class GremlinScriptFactory {

    public static GremlinScript<String> createGremlinScriptLiteral() {
        return new GremlinScriptLiteral();
    }

    public static GremlinScript<GraphTraversal> createGremlinScriptGraphTraversal() {
        return new GremlinScriptGraphTraversal();
    }
}
