/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class GremlinSourceBase implements GremlinSource {

    private String id;
    private String label;
    private Map properties;

    public GremlinSourceBase() {
        this.id = null;
        this.label = null;
        this.properties = new HashMap<String, String>();
    }

    public boolean hasProperty(String key) {
        return this.properties.get(key) != null;
    }

    @Override
    public void setProperty(String key, String value) {
        if (this.hasProperty(key) && value != null) {
            return;
        } else if (value == null) {
            this.properties.remove(key);
        } else {
            this.properties.put(key, value);
        }
    }
}
