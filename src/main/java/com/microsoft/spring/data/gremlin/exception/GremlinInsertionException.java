/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.exception;

import org.springframework.dao.DataAccessResourceFailureException;

public class GremlinInsertionException extends DataAccessResourceFailureException {

    public GremlinInsertionException(String msg) {
        super(msg);
    }

    public GremlinInsertionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
