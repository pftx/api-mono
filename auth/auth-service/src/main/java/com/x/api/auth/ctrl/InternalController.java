/**
 * InternalController.java
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.x.api.auth.dto.XInfoUser;
import com.x.api.common.dto.GenericResponse;
import com.x.api.common.xauth.XTokenUtil;
import com.x.api.common.xauth.token.SecuredXToken;

import io.swagger.annotations.ApiOperation;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 2, 2017
 */
@RestController
@RequestMapping(value = "/internal", method = {RequestMethod.GET, RequestMethod.POST})
public class InternalController {

    private static final Logger logger = LoggerFactory.getLogger(InternalController.class);

    @Value("${xtoken.authKey}")
    private String authKey;

    @RequestMapping(value = "/x-token", method = RequestMethod.GET)
    @ApiOperation(value = "Get an internal x-token.", httpMethod = "GET", produces = "application/json")
    public GenericResponse getXToken(Principal principal) {
        OAuth2Authentication authentication = (OAuth2Authentication) principal;
        XInfoUser user = (XInfoUser) authentication.getPrincipal();
        SecuredXToken xToken = new SecuredXToken(user.getExtension(), user.getPassword());
        String xTokenEnc = XTokenUtil.encodeToken(xToken, authKey);
        logger.info("/internal/x-token, user: {}, x-token: {}.", user.getUsername(), xTokenEnc);
        return new GenericResponse("x-token", xTokenEnc);
    }

}
