/**
 * AuthorizationException.java
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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.x.api.common.util.Constants;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizationException extends ServiceException {

    private static final long serialVersionUID = 564543920992298819L;

    public static AuthorizationException noPermissionToOperate(String entityType, long id) {
        return new AuthorizationException(String.format(Constants.MSG_NO_PERM_TO_OP, entityType, id));
    }

    public static AuthorizationException noPermissionToRead(String entityType, long id) {
        return new AuthorizationException(String.format(Constants.MSG_NO_PERM_TO_READ, entityType, id));
    }

    public AuthorizationException() {
        super(Constants.MSG_NO_PERMISSION);
    }

    public AuthorizationException(Map<String, Object> extension) {
        super(extension);
    }

    public AuthorizationException(String message, Map<String, Object> extension) {
        super(message, extension);
    }

    public AuthorizationException(String message, Throwable cause, Map<String, Object> extension) {
        super(message, cause, extension);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Throwable cause, Map<String, Object> extension) {
        super(cause, extension);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }

}
