/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import com.microsoft.spring.data.gremlin.annotation.EdgeFrom;
import com.microsoft.spring.data.gremlin.annotation.EdgeTo;
import com.microsoft.spring.data.gremlin.common.Constants;
import com.microsoft.spring.data.gremlin.common.GremlinUtils;
import com.microsoft.spring.data.gremlin.exception.UnexpectedGremlinSourceTypeException;
import com.microsoft.spring.data.gremlin.mapping.GremlinPersistentEntity;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.ConvertingPropertyAccessor;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.reflect.Field;

@NoArgsConstructor
public class GremlinSourceEdgeReader implements GremlinSourceReader {

    @Override
    public <T extends Object> T read(@NonNull Class<T> type, @NonNull MappingGremlinConverter converter,
                                     @NonNull GremlinSource source) {
       if (!(source instanceof GremlinSourceEdge)) {
           throw new UnexpectedGremlinSourceTypeException("should be instance of GremlinSourceEdge");
       }

       final T domain = GremlinUtils.createInstance(type);
       final ConvertingPropertyAccessor accessor = converter.getPropertyAccessor(domain);
       final GremlinPersistentEntity persistentEntity = converter.getPersistentEntity(type);

       for (final Field field : type.getDeclaredFields()) {
           final PersistentProperty property = persistentEntity.getPersistentProperty(field.getName());
           Assert.notNull(property, "persistence property should not be null");

           if (field.getName().equals(Constants.PROPERTY_ID) || field.getAnnotation(Id.class) != null) {
               accessor.setProperty(property, source.getId());
               continue;
           } else if (field.getAnnotation(EdgeFrom.class) != null || field.getAnnotation(EdgeTo.class) != null) {
               // We cannot do that here as the gremlin will not tell more information about vertex except Id. After the
               // query of Edge end, we can get the Id of vertex from/to. And then we will do extra 2 query to obtain
               // the 2 vertex and complete the edge.
               // That work will be wrapped in GremlinTemplate insert, and skip the property here.
               continue;
           }

           accessor.setProperty(property, source.getProperties().get(field.getName()));
       }

       return domain;
    }
}
