/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import com.microsoft.spring.data.gremlin.annotation.EdgeSet;
import com.microsoft.spring.data.gremlin.annotation.VertexSet;
import com.microsoft.spring.data.gremlin.common.GremlinEntityType;

public abstract class GremlinSourceFactory {

    public static GremlinSource createGremlinSource(GremlinEntityType type) {
        switch (type) {
            case VERTEX:
                return createGremlinSourceVertex();
            case EDGE:
                return createGremlinSourceEdge();
            case GRAPH:
                return createGremlinSourceGraph();
            default:
                throw new IllegalStateException("unexpected GremlinEntityType value");
        }
    }

    public static GremlinSource createGremlinSource(Class annotation) {
        if (annotation.equals(VertexSet.class)) {
            return new GremlinSourceVertex();
        } else if (annotation.equals(EdgeSet.class)) {
            return new GremlinSourceEdge();
        } else {
            throw new IllegalStateException("unexpected GremlinEntityType value");
        }
    }

    public static GremlinSource createGremlinSourceVertex() {
        return new GremlinSourceVertex();
    }

    public static GremlinSource createGremlinSourceEdge() {
        return new GremlinSourceVertex();
    }

    public static GremlinSource createGremlinSourceGraph() {
        return new GremlinSourceGraph();
    }
}
