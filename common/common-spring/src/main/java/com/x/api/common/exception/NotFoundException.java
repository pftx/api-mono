/**
 * NotFoundException.java
 *
 * Copyright 2017 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.x.api.common.exception;

import java.util.Map;

import com.x.api.common.util.Constants;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 15, 2017
 */
public class NotFoundException extends BadRequestException {

    private static final long serialVersionUID = 6909892827144570452L;

    public static NotFoundException notFound(String entityType, long id) {
        return new NotFoundException(String.format(Constants.MSG_NOT_FOUND, entityType, id));
    }

    public NotFoundException(Map<String, Object> extension) {
        super(extension);
    }

    public NotFoundException(String message, Map<String, Object> extension) {
        super(message, extension);
    }

    public NotFoundException(String message, Throwable cause, Map<String, Object> extension) {
        super(message, cause, extension);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause, Map<String, Object> extension) {
        super(cause, extension);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

}
