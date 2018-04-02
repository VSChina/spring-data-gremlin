/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import org.springframework.lang.NonNull;

public class BasicGremlinSourceWriter {

    private GremlinEntityInformation entityInformation;

    public BasicGremlinSourceWriter(@NonNull Object domain) {
        this.entityInformation = new GremlinEntityInformation(domain);
    }

    protected String getPersistentEntityId() {
        return entityInfo.getIdField().toString();
    }

    private String getPersistentEntityLabel() {
        return entityInfo.getIdField().toString();
    }

    protected void setGremlinSourceReserved(@NonNull GremlinSource source) {

        source.setId(this.getPersistentEntityId());
        source.setLabel(this.getPersistentEntityLabel());
    }
}
