/**
 * ValidationException.java
 *
 * Copyright 2017 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.x.api.common.exception;

import java.util.Map;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 21, 2017
 */
public class ValidationException extends BadRequestException {
    
    private static final long serialVersionUID = 8368802729179639360L;

    private static final String CONSISTENCY = "The value of %s from %s and %s must match.";
    private static final String CAN_NOT_MODIFY = "The '%s' can not be modified.";

    public static ValidationException consistency(String fieldName, String entityA, String entityB) {
        throw new ValidationException(String.format(CONSISTENCY, fieldName, entityA, entityB));
    }

    public static ValidationException canNotModify(String fieldName) {
        throw new ValidationException(String.format(CAN_NOT_MODIFY, fieldName));
    }

    public ValidationException(Map<String, Object> extension) {
        super(extension);
    }

    public ValidationException(String message, Map<String, Object> extension) {
        super(message, extension);
    }

    public ValidationException(String message, Throwable cause, Map<String, Object> extension) {
        super(message, cause, extension);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause, Map<String, Object> extension) {
        super(cause, extension);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

}
