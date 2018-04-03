/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion;

import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class BasicGremlinSourceReader {

    <T> T createDomainInstance(@NonNull Class<T> type) {
        final T domain;

        try {
             domain = type.newInstance();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("can not access type constructor", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("failed to create instance of given type", e);
        }

        return domain;
    }
}
