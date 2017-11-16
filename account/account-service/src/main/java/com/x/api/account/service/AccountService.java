/**
 * AccountService.java
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.x.api.account.mapper.AccountDao;
import com.x.api.account.model.Account;
import com.x.api.common.exception.BadRequestException;
import com.x.api.common.exception.NotFoundException;
import com.x.api.common.util.Constants;
import com.x.api.common.xauth.XAuthUtil;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    public Account getAccount(Authentication auth, long accountId) {
        XAuthUtil.checkCanOperate(auth, accountId);
        Account account = accountDao.findById(accountId);
        if (account == null) {
            throw NotFoundException.notFound(Constants.TYPE_ACCOUNT, accountId);
        }

        return account;
    }

    @PreAuthorize("hasAuthority('super_into')")
    public Account createAccount(Authentication auth, Account account) {
        try {
        accountDao.createAccount(account);
        return this.getAccount(auth, account.getAccountId());
        } catch (DuplicateKeyException e) {
            throw BadRequestException.duplicateKey(Constants.TYPE_ACCOUNT, "name", account.getName());
        }
    }

}
