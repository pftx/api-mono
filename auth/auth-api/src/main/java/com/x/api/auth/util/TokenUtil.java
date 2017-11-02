/**
 * TokenUtil.java
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
package com.x.api.auth.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.niolex.commons.compress.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.x.api.auth.dto.XTokenPrincipal;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 27, 2017
 */
public class TokenUtil {
    private static final String EXTRA_INFO_KEY = "extension";
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    public static OAuth2AccessToken addExtraInfo(OAuth2AccessToken accessToken, XTokenPrincipal info) {
        DefaultOAuth2AccessToken returnToken = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.putAll(returnToken.getAdditionalInformation());
        additionalInformation.put(EXTRA_INFO_KEY, info);
        returnToken.setAdditionalInformation(additionalInformation);
        return returnToken;
    }

    public static XTokenPrincipal extractExtraInfo(OAuth2AccessToken accessToken) {
        Object o = accessToken.getAdditionalInformation().get(EXTRA_INFO_KEY);
        if (o instanceof XTokenPrincipal) {
            return (XTokenPrincipal) o;
        }

        return null;
    }

    public static XTokenPrincipal extractExtraInfo(Map<String, Object> accessToken) {
        Object o = accessToken.get(EXTRA_INFO_KEY);
        if (o instanceof XTokenPrincipal) {
            return (XTokenPrincipal) o;
        }
        if (o == null) {
            return null;
        }
        try {
            String s = JacksonUtil.obj2Str(o);
            return JacksonUtil.str2Obj(s, XTokenPrincipal.class);
        } catch (Exception e) { // NOSONAR
            logger.warn("Invalid token extension: {}.", o);
            return null;
        }
    }

}
