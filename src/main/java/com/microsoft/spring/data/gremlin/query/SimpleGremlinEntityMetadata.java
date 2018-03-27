/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import com.microsoft.spring.data.gremlin.repository.support.GremlinEntityInformation;
import org.springframework.lang.NonNull;

public class SimpleGremlinEntityMetadata<T> implements GremlinEntityMetadata<T> {

    private final Class<T> type;
    private final GremlinEntityInformation<T, String> information;

    public SimpleGremlinEntityMetadata(@NonNull Class<T> type,
                                       @NonNull GremlinEntityInformation<T, String> information) {
        this.type = type;
        this.information = information;
    }

    @Override
    public Class<T> getJavaType() {
        return type;
    }

    @Override
    public String getCollectionName() {
        return information.getCollectionName();
    }
}
