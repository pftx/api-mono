/**
 * ExtraInfoTokenEnhancerService.java
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
package com.x.api.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.x.api.auth.dto.XInfoUser;
import com.x.api.auth.util.TokenUtil;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 27, 2017
 */
public class ExtraInfoTokenEnhancerService implements TokenEnhancer {

    /**
     * This is the override of super method.
     * 
     * @see org.springframework.security.oauth2.provider.token.TokenEnhancer#enhance(org.springframework.security.oauth2.common.OAuth2AccessToken,
     *      org.springframework.security.oauth2.provider.OAuth2Authentication)
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Authentication userAuthentication = authentication.getUserAuthentication();
        Object principal = userAuthentication.getPrincipal();
        if (principal instanceof XInfoUser) {
            XInfoUser user = (XInfoUser) principal;
            return TokenUtil.addExtraInfo(accessToken, user.getExtension());
        }
        return accessToken;
    }

}
