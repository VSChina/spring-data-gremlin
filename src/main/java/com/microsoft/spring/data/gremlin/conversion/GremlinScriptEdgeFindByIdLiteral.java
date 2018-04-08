/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import com.microsoft.spring.data.gremlin.common.Constants;
import com.microsoft.spring.data.gremlin.exception.UnexpectedGremlinSourceTypeException;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide generateScript findById from GremlinSourceEdge.
 */
@NoArgsConstructor
public class GremlinScriptEdgeFindByIdLiteral implements GremlinScript<String> {

    @Override
    public String generateScript(@NonNull GremlinSource source) {
        if (!(source instanceof GremlinSourceEdge)) {
            throw new UnexpectedGremlinSourceTypeException("should be the instance of GremlinSourceEdge");
        }

        final List<String> scriptList = new ArrayList<>();
        final String id = source.getId();

        Assert.notNull(id, "id should not be null");

        scriptList.add(Constants.GREMLIN_PRIMITIVE_GRAPH);
        scriptList.add(String.format(Constants.GREMLIN_PRIMITIVE_EDGE, id));

        return String.join(Constants.GREMLIN_PRIMITIVE_INVOKE, scriptList);
    }
}
