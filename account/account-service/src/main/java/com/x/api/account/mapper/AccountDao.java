/**
 * AccountDao.java
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

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.x.api.account.model.Account;
import com.x.api.common.dao.QueryConstants;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
@Mapper
public interface AccountDao extends QueryConstants {

    String SELECT_COL =
            "SELECT account_id, name, description, status, type, account_balance, currency_code, timezone, created, modified FROM account";

    @Results(id = "resultMap$Account", value = {
            @Result(column = "account_id", property = "accountId"),
            @Result(column = "name", property = "name"),
            @Result(column = "description", property = "description"),
            @Result(column = "status", property = "status"),
            @Result(column = "type", property = "type"),
            @Result(column = "account_balance", property = "accountBalance"),
            @Result(column = "currency_code", property = "currencyCode"),
            @Result(column = "timezone", property = "timezone"),
            @Result(column = "created", property = "created"),
            @Result(column = "modified", property = "modified")})
    @Select(SELECT_COL + " WHERE account_id = #{accountId} AND " + VISIBLE)
    Account findById(@Param("accountId") long accountId);

    @ResultMap("resultMap$Account")
    @Select(SELECT_COL + " WHERE name like #{name} AND " + VISIBLE)
    List<Account> findByName(@Param("name") String name);

    @Insert("INSERT INTO account(name, description, status, type, account_balance, currency_code, timezone)"
            + " VALUES (#{name}, #{description}, #{status, javaType=status, jdbcType=TINYINT}, #{type, javaType=AccountType, jdbcType=TINYINT}, #{accountBalance}, #{currencyCode}, #{timezone})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId", keyColumn = "account_id")
    public long createAccount(Account account);

    @Update("UPDATE account SET status = 3 WHERE account_id = #{accountId} AND " + NOT_DELETED)
    int deleteAccount(@Param("accountId") long accountId);

    @Update("UPDATE account SET status = 0 WHERE account_id = #{accountId} AND " + ACTIVE)
    int deactivateAccount(@Param("accountId") long accountId);

    @Update("UPDATE account SET name = #{name}, description = #{description}, type = #{type}, account_balance = #{accountBalance}, currency_code = #{currencyCode}, timezone = #{timezone} WHERE account_id = #{accountId} AND " + VISIBLE)
    int updateAccount(Account account);

}
