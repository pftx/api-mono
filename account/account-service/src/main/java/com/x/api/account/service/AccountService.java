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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.x.api.account.mapper.AccountDao;
import com.x.api.account.mapper.UserAccountDao;
import com.x.api.account.model.Account;
import com.x.api.account.model.UserAccount;
import com.x.api.common.exception.BadRequestException;
import com.x.api.common.exception.NotFoundException;
import com.x.api.common.util.Constants;
import com.x.api.common.util.MiscUtil;
import com.x.api.common.util.ModelUtil;
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

    @Autowired
    private UserAccountDao userAccountDao;

    @Cacheable(cacheNames = "acct/account", key = "#accountId")
    public Account getAccount(Authentication auth, long accountId) {
        XAuthUtil.checkCanRead(auth, accountId);
        Account account = accountDao.findById(accountId);
        if (account == null) {
            throw NotFoundException.notFound(Constants.TYPE_ACCOUNT, accountId);
        }

        return account;
    }

    @PreAuthorize("hasAuthority('super_into')")
    public List<Account> getAccountsByName(Authentication auth, String name, int offset, int limit, String sortBy,
            String sortOrder) {
        Preconditions.checkNotNull(name);
        return accountDao.findByName(MiscUtil.sqlLike(name), offset, limit, MiscUtil.fieldNameToDbColumn(sortBy), sortOrder);
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

    @CachePut(cacheNames = "acct/account", key = "#account.accountId")
    @Transactional
    public Account updateAccount(Authentication auth, Account account) {
        long accountId = account.getAccountId();
        XAuthUtil.checkCanOperate(auth, accountId);
        Account existing = accountDao.findById(accountId);
        if (existing == null) {
            throw NotFoundException.notFound(Constants.TYPE_ACCOUNT, accountId);
        }
        ModelUtil.prepareUpdate(existing, account);
        accountDao.updateAccount(existing);
        return accountDao.findById(account.getAccountId());
    }

    @CacheEvict(cacheNames = "acct/account", key = "#accountId")
    public boolean deleteAccount(Authentication auth, long accountId) {
        XAuthUtil.checkCanOperate(auth, accountId);
        return accountDao.deleteAccount(accountId) > 0;
    }

    @CacheEvict(cacheNames = "acct/account", key = "#accountId")
    @PreAuthorize("hasAuthority('super_into')")
    public boolean activateAccount(Authentication auth, long accountId) {
        return accountDao.activateAccount(accountId) > 0;
    }

    @CacheEvict(cacheNames = "acct/account", key = "#accountId")
    @PreAuthorize("hasAuthority('super_into')")
    public boolean deactivateAccount(Authentication auth, long accountId) {
        return accountDao.deactivateAccount(accountId) > 0;
    }

    public List<UserAccount> getUsersByAccountId(Authentication auth, long accountId) {
        XAuthUtil.checkCanRead(auth, accountId);
        return userAccountDao.findByAccountId(accountId);
    }

}
