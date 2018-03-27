/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GremlinQuery {

    private final Map<String, Object> criteriaMap = new LinkedHashMap<>();

    public static GremlinQuery gremlinQuery(GremlinCriteria gremlinCriteria) {
        return new GremlinQuery(gremlinCriteria);
    }

    public GremlinQuery() {

    }

    public Map<String, Object> getCriteriaMap() {
        return this.criteriaMap;
    }

    public GremlinQuery(GremlinCriteria criteria) {
        final List<GremlinCriteria> criteriaChain = criteria.getCriteriaChain();

        for (final GremlinCriteria c : criteriaChain) {
            this.addCriteria(c);
        }
    }

    public GremlinQuery addCriteria(GremlinCriteriaDefinition criteriaDefinition) {
        final Object existed = this.criteriaMap.get(criteriaDefinition.getKey());

        if (existed != null) {
            throw new IllegalStateException("the GremlinCritera existed already");
        }

        this.criteriaMap.put(criteriaDefinition.getKey(), criteriaDefinition.getCriteriaObject());

        return this;
    }
}
