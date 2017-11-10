/**
 * NotAvailableProvider.java
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
package com.x.api.gateway.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.niolex.commons.compress.JacksonUtil;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.x.api.common.dto.ErrorResponse;
import com.x.api.common.spring.XUidFilter;
import com.x.api.common.util.Constants;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 3, 2017
 */
@Component
public class NotAvailableProvider implements ZuulFallbackProvider {

    /**
     * This is the override of super method.
     * @see org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider#getRoute()
     */
    @Override
    public String getRoute() {
        return null;
    }

    /**
     * This is the override of super method.
     * @see org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider#fallbackResponse()
     */
    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {

            @Override
            public InputStream getBody() throws IOException {
                String xUid = XUidFilter.getXUid();
                ErrorResponse body = new ErrorResponse(xUid, getRawStatusCode(), Constants.MSG_SERVICE_UNAVAILABLE);
                return new ByteArrayInputStream(JacksonUtil.obj2bin(body));
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(Constants.HEADER_X_ERR_MSG, Constants.MSG_SERVICE_UNAVAILABLE);
                return headers;
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.SERVICE_UNAVAILABLE;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return getStatusCode().value();
            }

            @Override
            public String getStatusText() throws IOException {
                return getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {}

        };
    }

}
