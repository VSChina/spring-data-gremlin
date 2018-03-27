/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;

public abstract class AbstractGremlinQuery implements RepositoryQuery {

    private final GremlinQueryMethod method;
    private final GremlinOperations operations;

    public AbstractGremlinQuery(GremlinQueryMethod method, GremlinOperations operations) {
        this.method = method;
        this.operations = operations;
    }

    protected abstract GremlinQuery createQuery(GremlinParameterAccessor accessor);

    protected abstract boolean isDeleteQuery();

    public GremlinQueryExecution getExecution(GremlinQuery query, GremlinParameterAccessor accessor) {
        if (this.isDeleteQuery()) {
            return new GremlinQueryExecution.DeleteExecution(operations);
        } else {
            return new GremlinQueryExecution.MultiEntityExecution(operations);
        }
    }

    @Override
    public GremlinQueryMethod getQueryMethod() {
        return this.method;
    }

    @Override
    public Object execute(Object[] parameters) {
        final GremlinParameterAccessor accessor = new GremlinParametersParameterAccessor(method, parameters);
        final ResultProcessor processor = this.method.getResultProcessor().withDynamicProjection(accessor);
        final GremlinQuery query = createQuery(accessor);
        final GremlinQueryExecution execution = getExecution(query, accessor);

        // final String collectionName = ((GremlinEntityMetadata) method.getEntityInformation()).getCollectionName();

        return execution.execute(query, processor.getReturnedType().getDomainType());
    }
}
