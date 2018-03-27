/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository.support;

import com.microsoft.spring.data.gremlin.query.GremlinOperations;
import com.microsoft.spring.data.gremlin.repository.GremlinRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleGremlinRepository<T, ID extends Serializable> implements GremlinRepository<T, ID> {

    private final GremlinOperations operations;
    private final GremlinEntityInformation<T, ID> entityInformation;

    public SimpleGremlinRepository(GremlinEntityInformation<T, ID> entityInformation,
                                   ApplicationContext context) {
        this.operations = context.getBean(GremlinOperations.class);
        this.entityInformation = entityInformation;
    }

    public SimpleGremlinRepository(GremlinEntityInformation<T, ID> entityInformation,
                                   GremlinOperations operations) {
        this.operations = operations;
        this.entityInformation = entityInformation;
    }

    @Override
    public <S extends T> S save(@NonNull S entity) {

        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {

        return entities;
    }

    @Override
    public Iterable<T> findAll() {
        final List<T> entities = new ArrayList<>();

        return entities;
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        final List<T> entities = new ArrayList<>();

        return entities;
    }

    @Override
    public Optional<T> findById(@NonNull ID id) {
        return Optional.empty();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {

    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }
}

