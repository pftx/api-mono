/**
 * XUidFilter.java
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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.niolex.commons.test.MockUtil;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 2, 2017
 */
@Component
@Order(-10)
public class XUidFilter extends OncePerRequestFilter {
    public static final String X_UID = "X-Uid";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String xUid = request.getHeader(X_UID);
            if (xUid == null) {
                xUid = MockUtil.randString(12);
                response.addHeader(X_UID, xUid);
            }
            MDC.put("mdcData", xUid);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    public static final String getXUid() {
        String xUid = MDC.get("mdcData");
        if (xUid == null) {
            xUid = MockUtil.randString(12);
        }
        return xUid;
    }

}