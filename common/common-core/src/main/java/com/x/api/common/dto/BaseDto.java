/**
 * BaseDto.java
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.x.api.common.dto.annotation.IgnoreInput;
import com.x.api.common.validation.annotation.MyNotNull;
import com.x.api.common.validation.annotation.ValidDateTime;
import com.x.api.common.validation.annotation.ValidEnum;
import com.x.api.common.validation.groups.Create;

import lombok.Data;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 15, 2017
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "status", "created", "modified" })
public class BaseDto {

    @MyNotNull(groups = Create.class)
    @ValidEnum(supportedList = {"Active", "Inactive"})
    private String status;

    @ValidDateTime
    @IgnoreInput
    private String created;

    @ValidDateTime
    @IgnoreInput
    private String modified;

}
