/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.annotation;

import org.springframework.data.annotation.Persistent;

import java.lang.annotation.*;

/**
 * Specifies the field as VertexSet of graph.
 * @author Incarnation-p-lee
 */
@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface VertexSet {
}
