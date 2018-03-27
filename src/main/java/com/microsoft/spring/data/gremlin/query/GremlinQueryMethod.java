/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import com.microsoft.spring.data.gremlin.repository.support.GremlinEntityInformation;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.EntityMetadata;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;

import java.lang.reflect.Method;

public class GremlinQueryMethod extends QueryMethod {

    private GremlinEntityMetadata<?> metadata;

    public GremlinQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
        super(method, metadata, factory);
    }

    @Override
    public EntityMetadata<?> getEntityInformation() {
        final Class<?> domainClass = this.getDomainClass();
        final GremlinEntityInformation<Object, String> information = new GremlinEntityInformation(domainClass);

        this.metadata = new SimpleGremlinEntityMetadata(domainClass, information);

        return this.metadata;
    }
}
