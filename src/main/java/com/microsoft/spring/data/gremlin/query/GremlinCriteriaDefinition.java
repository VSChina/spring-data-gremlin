/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

public interface GremlinCriteriaDefinition {
    Object getCriteriaObject();

    String getKey();
}
