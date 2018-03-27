/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

import org.springframework.data.repository.query.ParametersParameterAccessor;

import java.util.Arrays;
import java.util.List;

public class GremlinParametersParameterAccessor extends ParametersParameterAccessor
        implements GremlinParameterAccessor {

    private final List<Object> values;

    public GremlinParametersParameterAccessor(GremlinQueryMethod method, Object[] values) {
        super(method.getParameters(), values);

        this.values = Arrays.asList(values);
    }

    @Override
    public Object[] getValues() {
        return values.toArray();
    }
}

