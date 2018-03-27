/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository.support;

import com.microsoft.spring.data.gremlin.query.GremlinOperations;
import com.microsoft.spring.data.gremlin.query.GremlinQueryMethod;
import com.microsoft.spring.data.gremlin.query.PartTreeGremlinQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Optional;

public class GremlinRepositoryFactory extends RepositoryFactorySupport {

    private final ApplicationContext context;
    private final GremlinOperations operations;

    public GremlinRepositoryFactory(GremlinOperations operations, ApplicationContext context) {
        this.context = context;
        this.operations = operations;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return SimpleGremlinRepository.class;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation information) {
        final EntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType());

        return getTargetRepositoryViaReflection(information, entityInformation, this.context);
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key,
                                                                   EvaluationContextProvider provider) {
        return Optional.of(new GremlinQueryLookupStrategy(operations, provider));
    }

    @Override
    public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return new GremlinEntityInformation<>(domainClass);
    }

    private static class GremlinQueryLookupStrategy implements QueryLookupStrategy {
        private final GremlinOperations operations;

        public GremlinQueryLookupStrategy(GremlinOperations operations, EvaluationContextProvider provider) {
            this.operations = operations;
        }

        @Override
        public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
                                            NamedQueries namedQueries) {
            final GremlinQueryMethod queryMethod = new GremlinQueryMethod(method, metadata, factory);

            Assert.notNull(queryMethod, "queryMethod must not be null");
            Assert.notNull(this.operations, "operations must not be null");

            return new PartTreeGremlinQuery(queryMethod, this.operations);
        }
    }
}

