/**
 * ServiceException.java
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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.base.Preconditions;
import com.x.api.common.util.ExceptionUtil;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 2, 2017
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class ServiceException extends RuntimeException {

    /**
     * Generated Uid.
     */
    private static final long serialVersionUID = 3082323860633678953L;
    private static final String DEFAULT_MSG =
            "We encountered an internal error. Please contact our client services team or try again later.";

    private Map<String, Object> extension;

    /**
     * Constructor
     */
    public ServiceException() {
        this(DEFAULT_MSG);
    }

    public ServiceException(String message) {
        super(message);
        Preconditions.checkNotNull(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        Preconditions.checkNotNull(message);
    }

    public ServiceException(Throwable cause) {
        this(ExceptionUtil.getExceptionMessage(cause), cause);
    }

    public ServiceException(Map<String, Object> extension) {
        this();
        this.extension = extension;
    }

    public ServiceException(String message, Map<String, Object> extension) {
        this(message);
        this.extension = extension;
    }

    public ServiceException(String message, Throwable cause, Map<String, Object> extension) {
        this(message, cause);
        this.extension = extension;
    }

    public ServiceException(Throwable cause, Map<String, Object> extension) {
        this(cause);
        this.extension = extension;
    }

    /**
     * @return the extension
     */
    public Map<String, Object> getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(Map<String, Object> extension) {
        this.extension = extension;
    }

}
