/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import com.microsoft.spring.data.gremlin.mapping.GremlinPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.parser.PartTree;

public class PartTreeGremlinQuery extends AbstractGremlinQuery {

    private final PartTree tree;
    private final MappingContext<?, GremlinPersistentProperty> context;
    private final ResultProcessor processor;

    public PartTreeGremlinQuery(GremlinQueryMethod method, GremlinOperations operations) {
        super(method, operations);

        this.processor = method.getResultProcessor();
        this.tree = new PartTree(method.getName(), processor.getReturnedType().getDomainType());
        this.context = operations.getConverter().getMappingContext();
    }

    @Override
    protected GremlinQuery createQuery(GremlinParameterAccessor accessor) {
        final GremlinQueryCreator creator = new GremlinQueryCreator(this.tree, accessor, this.context);
        final GremlinQuery query = creator.createQuery();

        if (this.tree.isLimiting()) {
            throw new IllegalStateException("Limiting is not supported.");
        }

        return query;
    }

    @Override
    protected boolean isDeleteQuery() {
        return this.tree.isDelete();
    }
}
