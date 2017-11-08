/**
 * AuthService.java
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
package com.x.api.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.x.api.common.xauth.XTokenPrincipal;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 31, 2017
 */
@Service
public class AuthService {

    private static final String UPDATE_LAST_LOGIN =
            "UPDATE user_account SET last_login = NOW() WHERE user_id = ? AND account_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean updateLastLogin(XTokenPrincipal info) {
        return jdbcTemplate.update(UPDATE_LAST_LOGIN, info.getOpUserId(), info.getOpAccountId()) > 0;
    }

}
