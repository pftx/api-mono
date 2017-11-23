/**
 * CollectionResponseBuilder.java
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

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.base.Preconditions;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 23, 2017
 */
public class CollectionResponseBuilder {

    private final ServletUriComponentsBuilder uriBuilder;
    private PageInfo paging;

    private CollectionResponseBuilder(ServletUriComponentsBuilder uriBuilder) {
        super();
        this.uriBuilder = uriBuilder;
    }

    public static CollectionResponseBuilder withHttpServletRequest(HttpServletRequest request) {
        return new CollectionResponseBuilder(ServletUriComponentsBuilder.fromRequest(request));
    }

    public CollectionResponseBuilder withPageInfo(int offset, int limit, String sortBy, String sortOrder) {
        this.paging = new PageInfo(offset, limit, sortBy, sortOrder, 0, false);
        uriBuilder.replaceQueryParam("offset", offset);
        uriBuilder.replaceQueryParam("limit", limit);
        uriBuilder.replaceQueryParam("sortBy", sortBy);
        uriBuilder.replaceQueryParam("sortOrder", sortOrder);
        return this;
    }

    public <T> CollectionResponse<T> build(String type, List<T> data) {
        Preconditions.checkNotNull(data);
        Preconditions.checkNotNull(paging);
        if (data.size() > paging.getLimit()) {
            this.paging.setNumItems(paging.getLimit());
            this.paging.setHasMoreItems(true);
            data = data.subList(0, paging.getLimit());
        } else {
            this.paging.setNumItems(data.size());
        }
        return new CollectionResponse<T>(uriBuilder.toUriString(), type, paging, data);
    }

}
