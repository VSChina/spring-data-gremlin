/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query;

public interface GremlinQueryExecution {

    Object execute(GremlinQuery query, Class<?> type);

    final class MultiEntityExecution implements GremlinQueryExecution {

        private final GremlinOperations operations;

        public MultiEntityExecution(GremlinOperations operations) {
            this.operations = operations;
        }

        @Override
        public Object execute(GremlinQuery query, Class<?> type) {
            return operations.find(query, type);
        }
    }

    final class DeleteExecution implements GremlinQueryExecution {

        private final GremlinOperations operations;

        public DeleteExecution(GremlinOperations operations) {
            this.operations = operations;
        }

        @Override
        public Object execute(GremlinQuery query, Class<?> type) {
            return operations.delete(query, type);
        }
    }
}
