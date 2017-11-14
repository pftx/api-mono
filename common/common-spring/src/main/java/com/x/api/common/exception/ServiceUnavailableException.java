/**
 * ServiceUnavailableException.java
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
 * @since Nov 9, 2017
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends ServiceException {

    private static final long serialVersionUID = -782759352567867977L;

    public ServiceUnavailableException() {
        super(Constants.MSG_SERVICE_UNAVAILABLE);
    }

    public ServiceUnavailableException(Map<String, Object> extension) {
        super(extension);
    }

    public ServiceUnavailableException(String message, Map<String, Object> extension) {
        super(message, extension);
    }

    public ServiceUnavailableException(String message, Throwable cause, Map<String, Object> extension) {
        super(message, cause, extension);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(Throwable cause, Map<String, Object> extension) {
        super(cause, extension);
    }

    public ServiceUnavailableException(Throwable cause) {
        super(cause);
    }

}
