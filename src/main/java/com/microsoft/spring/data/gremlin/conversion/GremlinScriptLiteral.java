/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import com.microsoft.spring.data.gremlin.common.Constants;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class GremlinScriptLiteral implements GremlinScript<String> {

    private void generateProperties(final List<String> scriptList, final Map<String, String> properties) {
        Assert.notNull(scriptList, "scriptList should not be null");
        Assert.notNull(properties, "properties should not be null");

        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY, entry.getKey(), entry.getValue()));
        }
    }

    private List<String> generateVertexScript(@NonNull GremlinSource source) {
        final List<String> scriptList = new ArrayList<>();
        final String label = source.getLabel();
        final String id = source.getId();
        final Map<String, String> properties = source.getProperties();

        Assert.notNull(label, "label should not be null");
        Assert.notNull(id, "id should not be null");
        Assert.notNull(properties, "properties should not be null");
        Assert.isTrue(source instanceof GremlinSourceVertex, "should be vertex from GremlinSource");

        scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_ADD_VERTEX, label));
        scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY, Constants.PROPERTY_ID, id));

        this.generateProperties(scriptList, properties);

        return scriptList;
    }

    private List<String> generateEdgeScript(@NonNull GremlinSource source) {
        final List<String> scriptList = new ArrayList<>();
        final String label = source.getLabel();
        final String id = source.getId();
        final Map<String, String> properties = source.getProperties();

        Assert.notNull(label, "label should not be null");
        Assert.notNull(id, "id should not be null");
        Assert.notNull(properties, "properties should not be null");
        Assert.isTrue(source instanceof GremlinSourceEdge, "should be edge from GremlinSource");

        final GremlinSourceEdge sourceEdge = (GremlinSourceEdge) source;
        final String vertexIdFrom = sourceEdge.getVertexIdFrom();
        final String vertexIdTo = sourceEdge.getVertexIdTo();

        Assert.notNull(vertexIdFrom, "vertexIdFrom should not be null");
        Assert.notNull(vertexIdTo, "vertexIdTo should not be null");

        scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_VERTEX, vertexIdFrom));
        scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_ADD_EDGE, label));
        scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_TO_VERTEX, vertexIdTo));
        scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_PROPERTY, Constants.PROPERTY_ID, id));

        this.generateProperties(scriptList, properties);

        return scriptList;
    }

    private List<String> generateGraphScript(GremlinSource source) {
        Assert.isTrue(source instanceof GremlinSourceGraph, "should be graph from GremlinSource");

        final List<String> scriptList = new ArrayList<>();
        final GremlinSourceGraph sourceGraph = (GremlinSourceGraph) source;

        for (final GremlinSource vertex : sourceGraph.getVertexSet()) {
            scriptList.addAll(this.generateVertexScript(vertex));
        }

        for (final GremlinSource edge : sourceGraph.getEdgeSet()) {
            scriptList.addAll(this.generateEdgeScript(edge));
        }

        return scriptList;
    }

    @Override
    public String generateScript(GremlinSource gremlinSource) {
        List<String> scriptList;
        final List<String> finalScript = new ArrayList<>();

        if (gremlinSource instanceof GremlinSourceVertex) {
            scriptList = generateVertexScript(gremlinSource);
        } else if (gremlinSource instanceof GremlinSourceEdge) {
            scriptList = generateEdgeScript(gremlinSource);
        } else if (gremlinSource instanceof GremlinSourceGraph) {
            scriptList = generateGraphScript(gremlinSource);
        } else {
            throw new IllegalStateException("Unknown GremlinSource Entity type.");
        }

        finalScript.add(Constants.GREMLIN_PRIMITIVE_GRAPH);
        finalScript.addAll(scriptList);

        return String.join(Constants.GREMLIN_PRIMITIVE_INVOKE, finalScript);
    }
}
