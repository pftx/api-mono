/**
 * UserAccountDao.java
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
package com.x.api.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.x.api.account.model.UserAccount;
import com.x.api.common.dao.QueryConstants;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2017年11月28日
 */
@Mapper
public interface UserAccountDao extends QueryConstants {

    String SELECT_COL = "SELECT user_id, username, status, permission, last_login FROM users JOIN user_account USING (user_id)";

    @Results(id = "resultMap$UserAccount", value = {
            @Result(column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "status", property = "status"),
            @Result(column = "permission", property = "permission"),
            @Result(column = "last_login", property = "lastLogin") })
    @Select(SELECT_COL + " WHERE account_id = #{accountId} AND " + VISIBLE)
    List<UserAccount> findByAccountId(@Param("accountId") long accountId);

}
