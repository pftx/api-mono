/**
 * ExtraInfoUserDetailsService.java
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
package com.x.api.auth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.x.api.auth.dto.XInfoUser;
import com.x.api.auth.dto.XTokenPrincipal;
import com.x.api.common.enums.Status;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 27, 2017
 */
public class ExtraInfoUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(ExtraInfoUserDetailsService.class);
    private static final String SELECT_USER =
            "SELECT user_id, username, password, email, status, locked, expired, password_modified FROM users WHERE username = ?";
    private static final String SELECT_AUTHORITIES =
            "SELECT authority FROM authorities WHERE username = ? AND status = 1";

    private static final RowMapper<XInfoUser> BASIC_INFO_MAPPER = (rs, rowNum) -> {
        XTokenPrincipal ext = new XTokenPrincipal();
        ext.setUserId(rs.getLong("user_id"));
        ext.setUserName(rs.getString("username"));
        ext.setOpUserId(rs.getLong("user_id"));
        return new XInfoUser(rs.getString("username"),
                rs.getString("password"),
                Status.isEnabled(rs.getInt("status")),
                true,
                !rs.getBoolean("expired"),
                !rs.getBoolean("locked"),
                ext);
    };

    private JdbcTemplate jdbcTemplate;

    /**
     * This is the override of super method.
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public XInfoUser loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Start to load user: {}.", username);
        
        XInfoUser user = jdbcTemplate.queryForObject(SELECT_USER, BASIC_INFO_MAPPER, username);
        List<String> authorities = jdbcTemplate.queryForList(SELECT_AUTHORITIES, String.class, username);
        user.setAuthorities(authorities);
        return user;
    }

    /**
     * @return the jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
