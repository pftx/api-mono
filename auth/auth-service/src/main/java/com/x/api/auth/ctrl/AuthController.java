/**
 * AuthController.java
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
package com.x.api.auth.ctrl;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.x.api.auth.dto.AccountInfo;
import com.x.api.auth.dto.XTokenPrincipal;
import com.x.api.auth.service.AuthService;
import com.x.api.auth.util.TokenUtil;
import com.x.api.common.dto.GenericResponse;
import com.x.api.common.exception.BadRequestException;

import io.swagger.annotations.ApiOperation;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 27, 2017
 */
@RestController
@RequestMapping(value = "/oauth", method = {RequestMethod.GET, RequestMethod.POST})
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${message:default}")
    private String message;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    @ApiOperation(value = "Test the configuration profile.", httpMethod = "GET", produces = "application/json")
    public GenericResponse hi() {
        return new GenericResponse("Hello " + message);
    }

    @RequestMapping(value = "/ext/user_info", method = RequestMethod.GET)
    @ApiOperation(value = "Get the current login user information.", httpMethod = "GET", produces = "application/json")
    public Principal user(Principal user) {
        logger.info("/oauth/ext/user_info, user: {}.", user.getName());
        return user;
    }

    @RequestMapping(value = "/ext/token_info", method = RequestMethod.GET)
    @ApiOperation(value = "Get the current token information.", httpMethod = "GET", produces = "application/json")
    public OAuth2AccessToken postAccessToken(Principal principal) {
        return getAccessToken(principal);
    }

    @RequestMapping(value = "/ext/switch_account", method = RequestMethod.POST)
    @ApiOperation(value = "Swith the operating account.", httpMethod = "POST", produces = "application/json")
    public OAuth2AccessToken switchUser(Principal principal, @RequestParam("op_account_id") Long opAccountId) {
        OAuth2Authentication authentication = (OAuth2Authentication) principal;
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(authentication);

        XTokenPrincipal info = TokenUtil.extractExtraInfo(accessToken);
        if (info == null || info.getAccountList() == null) {
            throw new BadRequestException("Your token don't support switch_account.");
        }
        for (AccountInfo ai : info.getAccountList()) {
            if (ai.getAccountId().equals(opAccountId)) {
                Long oldOpAccountId = info.getOpAccountId();
                info.setOpAccountId(opAccountId);
                authService.updateLastLogin(info);
                tokenStore.storeAccessToken(accessToken, authentication);
                logger.info("/oauth/ext/switch_account, user: {} switched the operating account from {} to {}.",
                        principal.getName(), oldOpAccountId, opAccountId);
                return accessToken;
            }
        }

        throw new BadRequestException("Invalid op_account_id.");
    }

    @RequestMapping(value = "/ext/revoke_access_token", method = RequestMethod.POST)
    @ApiOperation(value = "Revoke the current access token.", httpMethod = "POST", produces = "application/json")
    public GenericResponse revokeAccessToken(Principal principal) {
        OAuth2AccessToken accessToken = getAccessToken(principal);
        tokenStore.removeAccessToken(accessToken);
        String info = "Your access_token: " + accessToken.getValue() + " has been revoked.";
        GenericResponse resp = new GenericResponse(info);
        return resp;
    }

    @RequestMapping(value = "/ext/revoke_refresh_token", method = RequestMethod.POST)
    @ApiOperation(value = "Revoke the current refresh token together with access token.", httpMethod = "POST",
            produces = "application/json")
    public GenericResponse revokeRefreshToken(Principal principal) {
        OAuth2AccessToken accessToken = getAccessToken(principal);
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeAccessToken(accessToken);
        String response = "Your access_token: " + accessToken.getValue() + " has been revoked";
        if (refreshToken != null) {
            tokenStore.removeRefreshToken(refreshToken);
            response += ", and your refresh_token: " + refreshToken.getValue() + " has been revoked too";
        }
        String info = response + ".";
        GenericResponse resp = new GenericResponse(info);
        return resp;
    }

    private OAuth2AccessToken getAccessToken(Principal principal) {
        OAuth2Authentication authentication = (OAuth2Authentication) principal;
        return tokenStore.getAccessToken(authentication);
    }

}