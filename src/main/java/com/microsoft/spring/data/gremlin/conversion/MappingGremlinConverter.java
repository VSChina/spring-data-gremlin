/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import com.microsoft.spring.data.gremlin.annotation.EdgeFrom;
import com.microsoft.spring.data.gremlin.annotation.EdgeSet;
import com.microsoft.spring.data.gremlin.annotation.EdgeTo;
import com.microsoft.spring.data.gremlin.annotation.VertexSet;
import com.microsoft.spring.data.gremlin.common.Constants;
import com.microsoft.spring.data.gremlin.mapping.GremlinPersistentEntity;
import com.microsoft.spring.data.gremlin.mapping.GremlinPersistentProperty;
import com.microsoft.spring.data.gremlin.repository.support.GremlinEntityInformation;
import org.apache.tinkerpop.shaded.jackson.databind.DeserializationFeature;
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.convert.EntityConverter;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.ConvertingPropertyAccessor;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;


public class MappingGremlinConverter
        implements EntityConverter<GremlinPersistentEntity<?>, GremlinPersistentProperty, Object, GremlinSource>,
        ApplicationContextAware {

    protected final MappingContext<? extends GremlinPersistentEntity<?>, GremlinPersistentProperty> mappingContext;
    protected GenericConversionService conversionService;
    private ApplicationContext applicationContext;

    public MappingGremlinConverter(
            MappingContext<? extends GremlinPersistentEntity<?>, GremlinPersistentProperty> mapppingContext) {
        this.mappingContext = mapppingContext;
        this.conversionService = new GenericConversionService();
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public MappingContext<? extends GremlinPersistentEntity<?>, GremlinPersistentProperty> getMappingContext() {
        return this.mappingContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.applicationContext = context;
    }

    @Override
    public ConversionService getConversionService() {
        return this.conversionService;
    }

    @Override
    public <R extends Object> R read(Class<R> type, GremlinSource gremlinSource) {
        if (gremlinSource == null) {
            return null;
        }

        final GremlinPersistentEntity<?> entity = mappingContext.getPersistentEntity(type);
        Assert.notNull(entity, "entity should not be null");

        return readInternal(entity, type, gremlinSource);
    }

    protected <R extends Object> R readInternal(final GremlinPersistentEntity<?> entity, Class<R> type,
                                                final GremlinSource source) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(source.toString(), type);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read the gremlin source type", e);
        }
    }

    private String getObjectId(final Object embedded) {
        Assert.notNull(embedded, "embedded object should not be null");

        final GremlinPersistentEntity<?> persistent = this.mappingContext.getPersistentEntity(embedded.getClass());

        for (final Field field : embedded.getClass().getDeclaredFields()) {
            if (field.getName().equals(Constants.PROPERTY_ID)) {
                final PersistentProperty property = persistent.getPersistentProperty(Constants.PROPERTY_ID);
                Assert.notNull(property, "PersistentProperty should not be null");

                final Object idValue = this.getPropertyAccessor(embedded).getProperty(property);
                Assert.notNull(idValue, "idValue should not be null");

                return idValue.toString();
            }
        }

        throw new IllegalStateException("embedded object should have id field.");
    }

    private void writeAnnotation(Object domain, GremlinSource gremlinSource) {
        Assert.notNull(domain, "entity should not be null");
        Assert.notNull(gremlinSource, "GremlinSourceVertex should not be null");

        final GremlinEntityInformation entityInfo = new GremlinEntityInformation(domain.getClass());

        gremlinSource.setLabel(entityInfo.getEntityLabel());
    }

    private void writeField(final Object domain, final GremlinSource gremlinSource) {
        Assert.notNull(domain, "entity should not be null");
        Assert.notNull(gremlinSource, "GremlinSourceVertex should not be null");

        final ConvertingPropertyAccessor accessor = this.getPropertyAccessor(domain);
        final GremlinPersistentEntity<?> persistentEntity = this.mappingContext.getPersistentEntity(domain.getClass());

        for (final Field field : domain.getClass().getDeclaredFields()) {
            final PersistentProperty property = persistentEntity.getPersistentProperty(field.getName());
            Assert.notNull(property, "persistence property should not be null");
            final Object propertyObject = accessor.getProperty(property);

            if (field.getName().equals(Constants.PROPERTY_ID)) {
                gremlinSource.setId(propertyObject.toString());
                continue;
            } else if (gremlinSource instanceof GremlinSourceEdge) {
                final GremlinSourceEdge sourceEdge = (GremlinSourceEdge) gremlinSource;

                if (field.getAnnotation(EdgeFrom.class) != null) {
                    sourceEdge.setVertexIdFrom(this.getObjectId(propertyObject));
                    continue;
                } else if (field.getAnnotation(EdgeTo.class) != null) {
                    sourceEdge.setVertexIdTo(this.getObjectId(propertyObject));
                    continue;
                }
            }

            gremlinSource.setProperty(field.getName(), propertyObject.toString());
        }
    }

    private void writeEntity(Object domain, GremlinSource gremlinSource) {
        Assert.notNull(domain, "domain should not be null");
        Assert.notNull(gremlinSource, "GremlinSourceVertex should not be null");

        this.writeAnnotation(domain, gremlinSource);
        this.writeField(domain, gremlinSource);
    }

    private void writeGraphAnnotation(Object domain, Class annotation, final Field field,
                                      GremlinSourceGraph sourceGraph) {
        Assert.notNull(domain, "domain should not be null");
        Assert.notNull(annotation, "graph Annotation type should not be null");
        Assert.notNull(field, "field should not be null");
        Assert.notNull(sourceGraph, "GremlinSourceGraph should not be null");

        if (field.getAnnotation(annotation) != null) {
            final ConvertingPropertyAccessor accessor = this.getPropertyAccessor(domain);
            final GremlinPersistentEntity<?> persistent = this.mappingContext.getPersistentEntity(domain.getClass());
            Assert.notNull(persistent, "PersistenceEntity should not be null");

            final PersistentProperty property = persistent.getPersistentProperty(field.getName());
            Assert.notNull(property, "persistence property should not be null");

            final List<Object> objectList = (List<Object>) accessor.getProperty(property);

            if (objectList == null) {
                throw new IllegalStateException("ObjectList of Graph Annotation should not be null");
            }

            for (final Object object : objectList) {
                final GremlinSource source = GremlinSourceFactory.createGremlinSource(annotation);

                this.writeEntity(object, source);
                sourceGraph.sourceAdd(source);
            }
        }
    }

    private void writeGraph(Object domain, GremlinSource gremlinSource) {
        Assert.notNull(domain, "domain should not be null");
        Assert.notNull(gremlinSource, "GremlinSource should not be null");
        Assert.isTrue(gremlinSource instanceof GremlinSourceGraph, "should be GremlinSourceGraph instance");

        final GremlinSourceGraph sourceGraph = (GremlinSourceGraph) gremlinSource;

        for (final Field field : domain.getClass().getDeclaredFields()) {
            this.writeGraphAnnotation(domain, VertexSet.class, field, sourceGraph);
            this.writeGraphAnnotation(domain, EdgeSet.class, field, sourceGraph);
        }
    }

    @Override
    public void write(Object domain, GremlinSource gremlinSource) {
        if (domain == null) {
            return;
        } else if (gremlinSource == null) {
            return;
        }

        final GremlinEntityInformation entityInformation = new GremlinEntityInformation(domain.getClass());

        if (entityInformation.isEdgeEntity() || entityInformation.isVertexEntity()) {
            this.writeEntity(domain, gremlinSource);
        } else if (entityInformation.isGraphEntity()) {
            this.writeGraph(domain, gremlinSource);
        }
    }

    public GremlinSource convertToGremlinSource(Object domain) {
        if (domain == null) {
            return null;
        }

        final GremlinSource source;
        final GremlinEntityInformation entityInfo = new GremlinEntityInformation(domain.getClass());

        source = GremlinSourceFactory.createGremlinSource(entityInfo.getEntityType());
        this.write(domain, source);

        return source;
    }

    private ConvertingPropertyAccessor getPropertyAccessor(Object entity) {
        final GremlinPersistentEntity<?> persistentEntity = mappingContext.getPersistentEntity(entity.getClass());
        Assert.notNull(persistentEntity, "persistentEntity should not be null");

        final PersistentPropertyAccessor accessor = persistentEntity.getPropertyAccessor(entity);

        return new ConvertingPropertyAccessor(accessor, this.conversionService);
    }
}


