/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository.support;

import com.microsoft.spring.data.gremlin.annotation.Edge;
import com.microsoft.spring.data.gremlin.annotation.Graph;
import com.microsoft.spring.data.gremlin.annotation.Vertex;
import com.microsoft.spring.data.gremlin.common.Constants;
import com.microsoft.spring.data.gremlin.common.GremlinEntityType;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.core.support.AbstractEntityInformation;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

public class GremlinEntityInformation<T, ID> extends AbstractEntityInformation<T, ID> {

    private Field id;
    private String collectionName;
    private String entityLabel;
    private GremlinEntityType entityType;

    public GremlinEntityInformation(Class<T> domainClass) {
        super(domainClass);

        this.id = this.getIdField(domainClass);
        this.collectionName = this.getCollectionName(domainClass);
        this.entityType = this.getGremlinEntityType(domainClass);
        this.entityLabel = this.getEntityLabel(domainClass);
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public GremlinEntityType getEntityType() {
        return this.entityType;
    }

    public boolean isEdgeEntity() {
        return this.entityType == GremlinEntityType.EDGE;
    }

    public boolean isVertexEntity() {
        return this.entityType == GremlinEntityType.VERTEX;
    }

    public boolean isGraphEntity() {
        return this.entityType == GremlinEntityType.GRAPH;
    }

    public String getEntityLabel() {
        return this.entityLabel;
    }

    @Override
    public ID getId(T entity) {
        return (ID) ReflectionUtils.getField(this.id, entity);
    }

    @Override
    public Class<ID> getIdType() {
        return (Class<ID>) this.id.getType();
    }

    private Field getIdField(Class<?> domainClass) {
        Field idField;
        final List<Field> fields = FieldUtils.getFieldsListWithAnnotation(domainClass, Id.class);

        if (fields.isEmpty()) {
            idField = ReflectionUtils.findField(getJavaType(), Constants.PROPERTY_ID);
        } else if (fields.size() == 1) {
            idField = fields.get(0);
        } else {
            throw new IllegalArgumentException("only one @Id field is allowed");
        }

        if (idField == null) {
            throw new IllegalArgumentException("no @Id field found");
        } else if (idField.getType() != String.class) {
            throw new IllegalArgumentException("the type of @Id field should be String");
        }

        return idField;
    }

    private String getCollectionName(Class<?> domainClass) {
        final Graph annotation = domainClass.getAnnotation(Graph.class);

        if (annotation == null || annotation.collection() == null || annotation.collection().isEmpty()) {
            return domainClass.getSimpleName();
        } else {
            return annotation.collection();
        }
    }

    private GremlinEntityType getGremlinEntityType(Class<?> domainClass) {
        final Vertex vertexAnnotation = domainClass.getAnnotation(Vertex.class);

        if (vertexAnnotation != null) {
            return GremlinEntityType.VERTEX;
        }

        final Edge edgeAnnotation = domainClass.getAnnotation(Edge.class);

        if (edgeAnnotation != null) {
            return GremlinEntityType.EDGE;
        }

        final Graph graphAnnotation = domainClass.getAnnotation(Graph.class);

        if (graphAnnotation != null) {
            return GremlinEntityType.GRAPH;
        }

        return GremlinEntityType.UNKNOWN;
    }

    private String getEntityLabel(Class<?> domainClass) {
        String label = null;

        switch (this.entityType) {
            case VERTEX:
                final Vertex vertexAnnotation = domainClass.getAnnotation(Vertex.class);

                if (vertexAnnotation == null || vertexAnnotation.label().isEmpty()) {
                    label = domainClass.getSimpleName();
                } else {
                    label = vertexAnnotation.label();
                }
                break;
            case EDGE:
                final Edge edgeAnnotation = domainClass.getAnnotation(Edge.class);

                if (edgeAnnotation == null || edgeAnnotation.label().isEmpty()) {
                    label = domainClass.getSimpleName();
                } else {
                    label = edgeAnnotation.label();
                }
                break;
            case GRAPH:
                break;
            case UNKNOWN:
                // fallthrough
            default:
                throw new IllegalStateException("Unexpected gremlin entity type");
        }

        return label;
    }
}

