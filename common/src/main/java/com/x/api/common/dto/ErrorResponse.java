/**
 * ErrorResponse.java
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
package com.x.api.common.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 1, 2017
 */
@Data
@EqualsAndHashCode(callSuper=true)
@JsonPropertyOrder({ "@url", "@type", "uid", "status", "description", "extension" })
public class ErrorResponse extends GenericResponse {
    private static final String _TYPE = "error";

    private String uid;
    private int status;
    private String description;
    private Map<String, Object> extension;

    public ErrorResponse() {
        this.setType(_TYPE);
    }

    /**
     * Constructor.
     * 
     * @param uid
     * @param status
     * @param description
     */
    public ErrorResponse(String uid, int status, String description) {
        this();
        this.uid = uid;
        this.status = status;
        this.description = description;
    }

}
