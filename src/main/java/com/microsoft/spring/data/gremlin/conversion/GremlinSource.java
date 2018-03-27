/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import java.util.Map;

public interface GremlinSource {

    void setId(String id);

    void setLabel(String label);

    void setProperty(String key, String value);

    String getId();

    String getLabel();

    Map<String, String> getProperties();
}
