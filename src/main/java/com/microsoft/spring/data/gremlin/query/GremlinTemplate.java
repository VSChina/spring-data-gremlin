/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import com.microsoft.spring.data.gremlin.common.Constants;
import com.microsoft.spring.data.gremlin.common.GremlinFactory;
import com.microsoft.spring.data.gremlin.conversion.GremlinScriptFactory;
import com.microsoft.spring.data.gremlin.conversion.GremlinSource;
import com.microsoft.spring.data.gremlin.conversion.MappingGremlinConverter;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GremlinTemplate implements GremlinOperations, ApplicationContextAware {

    private final GremlinFactory gremlinFactory;
    private final MappingGremlinConverter mappingConverter;

    public GremlinTemplate(@NonNull GremlinFactory factory,
                           @NonNull MappingGremlinConverter converter) {
        this.gremlinFactory = factory;
        this.mappingConverter = converter;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void deleteAll() {
        final Client client = this.gremlinFactory.getGremlinClient();

        client.submit(Constants.GREMLIN_EDGE_DROP).all().join();
        client.submit(Constants.GREMLIN_VERTEX_DROP).all().join();
    }

    @Override
    public String getCollectionName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    @Override
    public MappingGremlinConverter getConverter() {
        return this.mappingConverter;
    }

    @Override
    public <T> List<T> find(GremlinQuery query, Class<T> entityClass) {
        final List<T> entities = new ArrayList<>();

        return entities;
    }

    @Override
    public <T> List<T> delete(GremlinQuery query, Class<T> entityClass) {
        final List<T> entities = new ArrayList<>();

        return entities;
    }

    @Override
    public <T> T insert(T object) {
        final GremlinSource gremlinSource = this.mappingConverter.convertToGremlinSource(object);
        final Client client = this.gremlinFactory.getGremlinClient();

        client.submit(GremlinScriptFactory.createGremlinScriptLiteral().generateScript(gremlinSource)).all().join();

        return object;
    }
}

