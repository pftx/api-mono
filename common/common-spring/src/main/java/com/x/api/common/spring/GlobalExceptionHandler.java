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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.x.api.common.dto.ErrorResponse;
import com.x.api.common.exception.ServiceException;
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

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getExtension());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleEverything(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return handleExceptionInternal(Constants.MSG_NO_PERMISSION, HttpStatus.FORBIDDEN, ex, null);
    }

    /**
     * This is the override of super method.
     * 
     * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException,
     *      org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus,
     *      org.springframework.web.context.request.WebRequest)
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> extension = new HashMap<String, Object>();
        for (ObjectError err : allErrors) {
            if (err instanceof FieldError) {
                extension.put(((FieldError) err).getField(), err.getDefaultMessage());
            } else {
                extension.put(err.getObjectName(), err.getDefaultMessage());
            }
        }
        return handleExceptionInternal(Constants.MSG_BAD_REQUEST_VALIDATION, HttpStatus.BAD_REQUEST, ex, extension);
    }

    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Map<String, Object> extension) {
        HttpStatus status = ExceptionUtil.findHttpStatus(ex);
        String message = ExceptionUtil.getExceptionMessage(ex);
        return handleExceptionInternal(message, status, ex, extension);
    }

    public ResponseEntity<Object> handleExceptionInternal(String message, HttpStatus status, Exception ex,
            Map<String, Object> extension) {
        String xUid = XUidFilter.getXUid();
        HttpHeaders headers = new HttpHeaders();

        if (status.is5xxServerError()) {
            logger.error("--XXX-- {} error occurred - {}.", status, xUid, ex);
        } else if (logger.isDebugEnabled()) {
            logger.debug("--DBG-- {} error occurred - {}.", status, xUid, ex);
        } else {
            logger.info("--YYY-- {} error occurred - {}, msg = {}", status, xUid, message);
        }

        headers.add(Constants.HEADER_X_ERR_MSG, message);
        ErrorResponse body = new ErrorResponse(xUid, status.value(), message);
        body.setExtension(extension);
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
        String message = ExceptionUtil.getExceptionMessage(ex);
        ErrorResponse body = new ErrorResponse(xUid, status.value(), message);
        headers.add(Constants.HEADER_X_ERR_MSG, message);
        return new ResponseEntity<Object>(body, headers, status);
    }

}
