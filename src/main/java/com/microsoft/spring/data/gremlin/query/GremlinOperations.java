/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import com.microsoft.spring.data.gremlin.conversion.MappingGremlinConverter;

import java.util.List;

public interface GremlinOperations {

    String getCollectionName(Class<?> entityClass);

    MappingGremlinConverter getConverter();

    void deleteAll();

    <T> List<T> find(GremlinQuery query, Class<T> entityClass);

    <T> List<T> delete(GremlinQuery query, Class<T> entityClass);

    <T> T insert(T object);
}
