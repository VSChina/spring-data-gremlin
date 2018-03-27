/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import java.util.ArrayList;
import java.util.List;

public class GremlinCriteria implements GremlinCriteriaDefinition {

    private String key;
    private Object value;
    private List<GremlinCriteria> criteriaChain;

    public GremlinCriteria(String key) {
        this(new ArrayList<>(), key);
    }

    protected GremlinCriteria(List<GremlinCriteria> criteriaChain, String key) {
        this.criteriaChain = criteriaChain;
        this.criteriaChain.add(this);
        this.key = key;
    }

    @Override
    public Object getCriteriaObject() {
        return this.value;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public static GremlinCriteria where(String key) {
        return new GremlinCriteria(key);
    }

    public GremlinCriteria is(Object object) {
        this.value = object;

        return this;
    }

    public GremlinCriteria and(Object object) {
        return new GremlinCriteria(this.criteriaChain, key);
    }

    public List<GremlinCriteria> getCriteriaChain() {
        return this.criteriaChain;
    }
}
