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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.niolex.commons.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.x.api.auth.dto.XInfoUser;
import com.x.api.common.dao.QueryConstants;
import com.x.api.common.enums.Status;
import com.x.api.common.helper.EnumHelper;
import com.x.api.common.xauth.AccountInfo;
import com.x.api.common.xauth.XTokenPrincipal;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 27, 2017
 */
public class ExtraInfoUserDetailsService implements UserDetailsService, QueryConstants {

    private static final Logger logger = LoggerFactory.getLogger(ExtraInfoUserDetailsService.class);
    private static final String SELECT_USER =
            "SELECT user_id, username, password, email, status, locked, expired, password_modified FROM users WHERE username = ?";
    private static final String SELECT_AUTHORITIES =
            "SELECT authority FROM authorities WHERE username = ? AND " + ACTIVE;
    private static final String SELECT_PERMISSIONS =
            "SELECT DISTINCT permission FROM permissions JOIN authority_permissions USING (permission) WHERE authority IN (:authorities) AND "
                    + ACTIVE;
    private static final String SELECT_ACCOUNTS =
            "SELECT account_id, name, status, permission FROM account JOIN user_account USING (account_id) WHERE user_id = ? AND "
                    + VISIBLE + " ORDER BY last_login DESC";

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

    private static final Map<String, List<String>> PERM_MAP = ImmutableMap.<String, List<String>>of(
            "account_admin", ImmutableList.of("account_admin", "account_write", "account_read"),
            "account_write", ImmutableList.of("account_write", "account_read"),
            "account_read", ImmutableList.of("account_read"));

    private static final RowMapper<AccountInfo> ACCOUNT_INFO_MAPPER = (rs, rowNum) -> {
        return new AccountInfo(rs.getLong("account_id"), rs.getString("name"),
                EnumHelper.parseFromInt(Status.class, rs.getInt("status")).name(),
                PERM_MAP.get(rs.getString("permission")));
    };

    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * This is the override of super method.
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public XInfoUser loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Start to load user: {}.", username);
        try {
            XInfoUser user = jdbcTemplate.queryForObject(SELECT_USER, BASIC_INFO_MAPPER, username);
            List<String> authorities = jdbcTemplate.queryForList(SELECT_AUTHORITIES, String.class, username);
            user.setAuthorities(authorities);

            XTokenPrincipal extension = user.getExtension();
            if (!CollectionUtil.isEmpty(authorities)) {
                List<String> permissions =
                        namedParameterJdbcTemplate.query(SELECT_PERMISSIONS,
                                Collections.singletonMap("authorities", authorities),
                                new SingleColumnRowMapper<String>(String.class));
                extension.setPermissionList(permissions);
            }

            List<AccountInfo> accountList = jdbcTemplate.query(SELECT_ACCOUNTS, ACCOUNT_INFO_MAPPER, user.getExtension().getUserId());
            extension.setAccountList(accountList);

            if (!CollectionUtil.isEmpty(accountList)) {
                extension.setOpAccountId(accountList.get(0).getAccountId());
            }

            return user;
        } catch (DataAccessException dae) {
            throw new UsernameNotFoundException("User " + username + " not found.", dae);
        }
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
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

}
