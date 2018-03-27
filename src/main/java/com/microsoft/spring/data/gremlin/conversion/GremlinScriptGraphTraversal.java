/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

public class GremlinScriptGraphTraversal implements GremlinScript<GraphTraversal> {

    @Override
    public GraphTraversal generateScript(GremlinSource gremlinSource) {
        return null;
    }
}
