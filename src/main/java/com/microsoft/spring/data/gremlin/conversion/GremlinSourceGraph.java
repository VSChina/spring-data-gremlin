/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class GremlinSourceGraph extends GremlinSourceBase {

    private List<GremlinSource> vertexSet;
    private List<GremlinSource> edgeSet;

    public GremlinSourceGraph() {
        super();

        this.vertexSet = new ArrayList<>();
        this.edgeSet = new ArrayList<>();
    }

    public void sourceAdd(GremlinSource source) {
        if (source instanceof GremlinSourceVertex) {
            this.vertexSet.add(source);
        } else if (source instanceof GremlinSourceEdge) {
            this.edgeSet.add(source);
        }
    }
}
