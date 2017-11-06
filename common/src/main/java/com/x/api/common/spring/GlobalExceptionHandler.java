/**
 * GlobalExceptionHandler.java
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
package com.x.api.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.x.api.common.dto.ErrorResponse;
import com.x.api.common.util.Constants;
import com.x.api.common.util.ExceptionUtil;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 1, 2017
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServiceException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = ExceptionUtil.findHttpStatus(ex);
        String xUid = XUidFilter.getXUid();
        String message = ExceptionUtil.getExceptionMessage(ex);

        if (status.is5xxServerError()) {
            logger.error("--XXX-- {} error occurred - {}.", status, xUid, ex);
        } else if (logger.isDebugEnabled()) {
            logger.debug("--DBG-- {} error occurred - {}.", status, xUid, ex);
        } else {
            logger.info("--YYY-- {} error occurred - {}, msg = {}.", status, xUid, message);
        }

        headers.add(Constants.HEADER_X_ERR_MSG, message);
        ErrorResponse body = new ErrorResponse(xUid, status.value(), message);
        return new ResponseEntity<Object>(body, headers, status);
    }

    /**
     * This is the override of super method.
     * 
     * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleExceptionInternal(java.lang.Exception,
     *      java.lang.Object, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus,
     *      org.springframework.web.context.request.WebRequest)
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object nobody, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String xUid = XUidFilter.getXUid();
        ErrorResponse body = new ErrorResponse(xUid, status.value(), ExceptionUtil.getExceptionMessage(ex));
        return new ResponseEntity<Object>(body, headers, status);
    }

}
