/**
 * AuditFilter.java
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
package com.x.api.gateway.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.niolex.commons.codec.StringUtil;
import org.apache.niolex.commons.compress.JacksonUtil;
import org.apache.niolex.commons.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.x.api.common.spring.XUidFilter;
import com.x.api.common.util.Constants;
import com.x.api.gateway.bean.AuditBean;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 6, 2017
 */
@Component
public class AuditFilter extends ZuulFilter {
    private static final Logger log = LoggerFactory.getLogger(AuditFilter.class);

    /**
     * This is the override of super method.
     * 
     * @see com.netflix.zuul.IZuulFilter#shouldFilter()
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * This is the override of super method.
     * 
     * @see com.netflix.zuul.IZuulFilter#run()
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        AuditBean bean = new AuditBean();
        bean.setRequestUrl(request.getRequestURL().toString());
        bean.setXUid(ctx.getResponse().getHeader(XUidFilter.X_UID));
        String remoteAddr = request.getRemoteAddr();
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        bean.setIp(getRealIp(remoteAddr, xForwardedFor));
        bean.setRemoteAddr(remoteAddr);
        bean.setXForwardedFor(xForwardedFor);
        long inTime = (Long) ctx.get(AccessFilter.X_IN_TIME);
        bean.setTime(DateTimeUtil.formatDate2LongStr(inTime));
        bean.setDuration((int) (System.currentTimeMillis() - inTime));

        bean.setStatus(ctx.getResponse().getStatus());
        ctx.getZuulResponseHeaders().stream().filter(p -> p.first().equalsIgnoreCase(Constants.HEADER_X_ERR_MSG))
                .findFirst().ifPresent(p -> bean.setErrorMsg(p.second()));
        bean.setMethod(ctx.getRequest().getMethod());
        bean.setQueryStr(ctx.getRequest().getQueryString());

        try {
            log.info(JacksonUtil.obj2Str(bean));
        } catch (IOException e) {
            log.error("Failed to write audit log.", e);
        }
        return null;
    }

    private static String getRealIp(String remoteAddr, String xForwardedFor) {
        if (StringUtil.isBlank(xForwardedFor)) {
            return remoteAddr;
        } else {
            int idx = xForwardedFor.indexOf(",");
            if (idx != -1) {
                return xForwardedFor.substring(0, idx).trim();
            } else {
                return xForwardedFor;
            }
        }
    }

    /**
     * This is the override of super method.
     * 
     * @see com.netflix.zuul.ZuulFilter#filterType()
     */
    @Override
    public String filterType() {
        return "post";
    }

    /**
     * This is the override of super method.
     * 
     * @see com.netflix.zuul.ZuulFilter#filterOrder()
     */
    @Override
    public int filterOrder() {
        return 0;
    }

}
