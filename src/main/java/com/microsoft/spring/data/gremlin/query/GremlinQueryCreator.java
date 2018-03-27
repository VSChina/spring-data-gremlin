/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import com.microsoft.spring.data.gremlin.mapping.GremlinPersistentProperty;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.PersistentPropertyPath;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import java.util.Iterator;

public class GremlinQueryCreator extends AbstractQueryCreator<GremlinQuery, GremlinCriteria> {

    private final MappingContext<?, GremlinPersistentProperty> context;

    public GremlinQueryCreator(PartTree tree, GremlinParameterAccessor accessor,
                               MappingContext<?, GremlinPersistentProperty> context) {
        super(tree, accessor);

        this.context = context;
    }

    private boolean isSimpleComparisionPossible(Part part) {
        switch (part.shouldIgnoreCase()) {
            case NEVER:
                return true;
            case WHEN_POSSIBLE:
                return part.getProperty().getType() != String.class;
            case ALWAYS:
                return false;
            default:
                return true;
        }
    }

    private GremlinCriteria createLikeRegexCriteriaOrThrow(Part part, GremlinPersistentProperty property,
                                                           GremlinCriteria criteria, Iterator<Object> parameters,
                                                           boolean b) {
        final PropertyPath path = part.getProperty().getLeafProperty();

        switch (part.shouldIgnoreCase()) {
            case ALWAYS:
                if (path.getType() != String.class) {
                    throw new IllegalArgumentException("part must be String, but: " + path.getType() + ", " + path);
                }

                return criteria;
            case WHEN_POSSIBLE:
                return criteria;
            case NEVER:
                break;
            default:
        }

        return null;
    }

    private GremlinCriteria from(Part part,
                                 GremlinPersistentProperty property, GremlinCriteria criteria,
                                 Iterator<Object> parameters) {
        final Part.Type type = part.getType();

        switch (type) {
            case SIMPLE_PROPERTY:
                if (isSimpleComparisionPossible(part)) {
                    return criteria.is(parameters.next());
                } else {
                    return createLikeRegexCriteriaOrThrow(part, property, criteria, parameters, false);
                }

            default:
                throw new IllegalArgumentException("unsupported keyword: " + type);
        }
    }

    @Override
    protected GremlinCriteria create(Part part, Iterator<Object> iterator) {
        final PersistentPropertyPath<GremlinPersistentProperty> path =
                context.getPersistentPropertyPath(part.getProperty());
        final GremlinPersistentProperty property = path.getLeafProperty();
        final GremlinCriteria criteria = this.from(part, property, GremlinCriteria.where(path.toDotPath()), iterator);

        return criteria;
    }

    @Override
    protected GremlinCriteria and(Part part, GremlinCriteria criteria, Iterator<Object> iterator) {
        if (criteria == null) {
            return this.create(part, iterator);
        }

        final PersistentPropertyPath<GremlinPersistentProperty> path =
                context.getPersistentPropertyPath(part.getProperty());
        final GremlinPersistentProperty property = path.getLeafProperty();

        return this.from(part, property, criteria.and(path.toDotPath()), iterator);
    }

    @Override
    protected GremlinQuery complete(GremlinCriteria criteria, Sort sort) {
        final GremlinQuery query = new GremlinQuery(criteria);

        return query;
    }

    @Override
    protected GremlinCriteria or(GremlinCriteria or, GremlinCriteria criteria) {
        throw new IllegalStateException("Criteria or is not supported.");
    }
}
