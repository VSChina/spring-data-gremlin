/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.common;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.ser.Serializers;
import org.springframework.lang.NonNull;

public class GremlinFactory {
    private Client client;
    private Cluster gremlinCluster;

    public GremlinFactory(@NonNull String endpoint, @NonNull String port,
                          @NonNull String username, @NonNull String password) {
        this.gremlinCluster = Cluster.build(endpoint).serializer(Serializers.DEFAULT_RESULT_SERIALIZER)
                .credentials(username, password).enableSsl(true).port(Integer.parseInt(port)).create();

        this.client = this.gremlinCluster.connect();
    }

    public Client getGremlinClient() {
        return this.client;
    }
}

