/**
 * AccessFilter.java
 *
 * Copyright 2016 the original author or authors.
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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.niolex.commons.compress.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.x.api.auth.client.AuthClient;
import com.x.api.auth.util.TokenUtil;
import com.x.api.common.dto.ErrorResponse;
import com.x.api.common.spring.XUidFilter;
import com.x.api.common.xauth.XTokenAuthenticationFilter;
import com.x.api.common.xauth.XTokenPrincipal;
import com.x.api.common.xauth.XTokenUtil;
import com.x.api.common.xauth.token.SecuredXToken;

import feign.FeignException;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.1
 * @since Dec 8, 2016
 */
@Component
public class AccessFilter extends ZuulFilter {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";
    private static final String NO_TOKEN_MSG = "Access token is required to access this resource.";
    private static final String BAD_TOKEN_MSG = "Invalid access token.";

    private static final Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Autowired
    private AuthClient authClient;

    @Value("${xtoken.authKey}")
    private String authKey;

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
        String xUid = XUidFilter.getXUid();
        ctx.addZuulRequestHeader(XUidFilter.X_UID, xUid);

        log.info("[{}] -> {}", request.getMethod(), request.getRequestURL().toString());

        String path = request.getRequestURI();
        if (path.startsWith("/oauth")) {
            log.debug("Skip auth check for /oauth endpoints.");
            return null;
        }

        // first check the header...
        String token = extractHeaderToken(request);
        if (token == null) {
            token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
        }
        if (token == null) {
            log.debug("Access token is empty.");
            generateResponse(ctx, xUid, HttpServletResponse.SC_UNAUTHORIZED, NO_TOKEN_MSG);
            return null;
        }
        log.debug("Access token retrieved: {}.", token);

        try {
            Map<String, Object> accessToken = authClient.checkToken(token);
            XTokenPrincipal extraInfo = TokenUtil.extractExtraInfo(accessToken);
            if (extraInfo == null) {
                generateResponse(ctx, xUid, HttpServletResponse.SC_UNAUTHORIZED, BAD_TOKEN_MSG);
                return null;
            }
            SecuredXToken xToken = new SecuredXToken(extraInfo, "xx-xx-xx");
            String rawToken = XTokenUtil.encodeToken(xToken, authKey);
            log.debug("Encoded x-token => ", rawToken);
            ctx.addZuulRequestHeader(XTokenAuthenticationFilter.HEADER_NAME, rawToken);
        } catch (FeignException e) { // NOSONAR.
            int status = e.status() == 400 ? HttpServletResponse.SC_UNAUTHORIZED : e.status();
            log.debug("Failed to get token details: {}", e.getMessage());
            generateResponse(ctx, xUid, status, BAD_TOKEN_MSG);
        }
        return null;
    }

    private void generateResponse(RequestContext ctx, String xUid, int httpStatus, String errorMessage) {
        ErrorResponse body = new ErrorResponse(xUid, httpStatus, errorMessage);
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(httpStatus);
        ctx.addZuulResponseHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE);
        try {
            ctx.setResponseBody(JacksonUtil.obj2Str(body));
        } catch (IOException e) {
            log.error("Error occurred when set response.", e);
        }
    }

    /**
     * Extract the OAuth bearer token from a header.
     * 
     * @param request The request.
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    protected String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                // Add this here for the auth details later. Would be better to change the signature of this method.
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
                        value.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim());
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }

        return null;
    }

    /**
     * This is the override of super method.
     * 
     * @see com.netflix.zuul.ZuulFilter#filterType()
     */
    @Override
    public String filterType() {
        return "pre";
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
