/**
 * CustomAccessDeniedHandler.java
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.CharEncoding;
import org.apache.niolex.commons.compress.JacksonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.x.api.common.dto.ErrorResponse;
import com.x.api.common.util.Constants;
import com.x.api.common.util.ExceptionUtil;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 1, 2017
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final int STATUS = HttpServletResponse.SC_FORBIDDEN;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException, ServletException {
        String xUid = XUidFilter.getXUid();
        ErrorResponse body = new ErrorResponse(xUid, STATUS, ExceptionUtil.getExceptionMessage(ex));
        response.setStatus(STATUS);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.addHeader(Constants.HEADER_X_ERR_MSG, body.getDescription());
        response.getWriter().write(JacksonUtil.obj2Str(body));
        response.flushBuffer();
    }

}
