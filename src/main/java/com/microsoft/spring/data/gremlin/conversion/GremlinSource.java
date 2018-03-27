/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import java.util.Map;

/**
 * Provider interface to obtain and store information from domain class.
 * For Vertex and Edge, they consist of id (String, Reserved), label (String, Reserved) and
 * a set of properties.
 * The property key should be String, and value can be one of String, number and boolean.
 */
public interface GremlinSource {
    /**
     * Set the id (String) of domain
     *
     * @return never
     */
    void setId(String id);

    /**
     * Set the label (String) of domain
     *
     * @return never
     */
    void setLabel(String label);

    /**
     * Set the property map (String) of domain
     *
     * @return never
     */
    void setProperty(String key, String value);

    /**
     * Get the id (String) of domain
     *
     * @return will never be null
     */
    String getId();

    /**
     * Get the label (String) of domain
     *
     * @return will never be null
     */
    String getLabel();

    /**
     * Get the properties (Map) of domain
     *
     * @return will never be null
     */
    Map<String, Object> getProperties();
}
