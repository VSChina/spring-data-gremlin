/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.common;

import com.microsoft.spring.data.gremlin.exception.GremlinIllegalConfigurationException;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.junit.Test;
import org.springframework.util.Assert;

public class GremlinFactoryUnitTest {

    @Test(expected = GremlinIllegalConfigurationException.class)
    public void testGremlinFactoryException() {
        final GremlinFactory factory = new GremlinFactory(TestConstants.FAKE_ENDPOINT, null,
                TestConstants.FAKE_USERNAME, TestConstants.FAKE_PASSWORD);

        factory.getGremlinClient();
    }

    @Test
    public void testGremlinFactoryNormal() {
        Client client;
        GremlinFactory factory;

        factory = new GremlinFactory(TestConstants.EMPTY_STRING, TestConstants.EMPTY_STRING,
                TestConstants.EMPTY_STRING, TestConstants.EMPTY_STRING);
        client = factory.getGremlinClient();

        Assert.isTrue(client.getCluster().getPort() == TestConstants.DEFAULT_ENDPOINT_PORT, "should be default port");
        Assert.isTrue(!client.getSettings().getSession().isPresent(), "should have no session without remote server");
    }
}
