/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GremlinSourceEdge extends GremlinSourceBase {

    private String vertexIdFrom;
    private String vertexIdTo;

    public GremlinSourceEdge() {
        super();

        this.vertexIdFrom = null;
        this.vertexIdTo = null;
    }
}
