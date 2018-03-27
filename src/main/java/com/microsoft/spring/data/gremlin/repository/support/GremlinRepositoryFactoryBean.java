/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository.support;

import com.microsoft.spring.data.gremlin.mapping.GremlinMappingContext;
import com.microsoft.spring.data.gremlin.query.GremlinOperations;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

public class GremlinRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends RepositoryFactoryBeanSupport<T, S, ID> implements ApplicationContextAware {

    private ApplicationContext context;
    private GremlinOperations operations;
    private boolean mappingContextConfigured = false;

    public GremlinRepositoryFactoryBean(Class<? extends T> repositoryInterface, GremlinOperations operations) {
        super(repositoryInterface);

        this.operations = operations;
    }

    protected RepositoryFactorySupport getFactoryInstance(ApplicationContext context) {
        return new GremlinRepositoryFactory(this.operations, context);
    }

    @Override
    protected final RepositoryFactorySupport createRepositoryFactory() {
        return this.getFactoryInstance(this.context);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    protected void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);

        this.mappingContextConfigured = true;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        if (this.mappingContextConfigured) {
            return;
        } else if (this.operations == null) {
            this.setMappingContext(new GremlinMappingContext());
        } else {
            this.setMappingContext(this.operations.getConverter().getMappingContext());
        }
    }
}

