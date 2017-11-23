/**
 * CollectionResponse.java
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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 23, 2017
 */
@Data
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"@url", "@type", "paging", "items"})
public class CollectionResponse<T> extends GenericResponse<Object> {
    private static final String _TYPE = "collection";

    private PageInfo paging;
    private List<T> items;

    public CollectionResponse() {
        super();
        this.setType(_TYPE);
    }

    public CollectionResponse(List<T> items) {
        this();
        this.items = items;
    }

    public CollectionResponse(PageInfo paging, List<T> items) {
        this();
        this.paging = paging;
        this.items = items;
    }

    public CollectionResponse(String type, PageInfo paging, List<T> items) {
        this();
        this.setType(type);
        this.paging = paging;
        this.items = items;
    }

    public CollectionResponse(String url, String type, PageInfo paging, List<T> items) {
        this();
        this.setUrl(url);
        this.setType(type);
        this.paging = paging;
        this.items = items;
    }

}
