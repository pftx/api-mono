/**
 * AccountController.java
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
package com.x.api.account.ctrl;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.x.api.account.dto.AccountDto;
import com.x.api.account.dto.UserAccountDto;
import com.x.api.account.model.Account;
import com.x.api.account.model.UserAccount;
import com.x.api.account.service.AccountService;
import com.x.api.common.dto.CollectionResponse;
import com.x.api.common.dto.CollectionResponseBuilder;
import com.x.api.common.dto.GenericResponse;
import com.x.api.common.helper.DateTimeHelper;
import com.x.api.common.util.DtoUtil;
import com.x.api.common.util.ValidationUtil;
import com.x.api.common.validation.annotation.ValidLimit;
import com.x.api.common.validation.annotation.ValidOffset;
import com.x.api.common.validation.annotation.ValidOrder;
import com.x.api.common.validation.annotation.ValidSortBy;
import com.x.api.common.validation.groups.Create;

import io.swagger.annotations.ApiOperation;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 27, 2017
 */
@RestController
@Validated
@RequestMapping(value = "/acct/accounts")
public class AccountController {

    private static final String MSG_NAME_LEN = "The length of 'name' must in range [3, 126].";
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Value("${message:default}")
    private String message;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    @ApiOperation(value = "Test the configuration profile.", httpMethod = "GET", produces = "application/json")
    public GenericResponse<String> hi() {
        return new GenericResponse<String>("Hello " + message);
    }

    @RequestMapping(value = "/user_info", method = RequestMethod.GET)
    @ApiOperation(value = "Get the current login user information.", httpMethod = "GET", produces = "application/json")
    public Principal user(Principal user) {
        logger.info("/acct/accounts/user_info, user: {}.", user.getName());
        return user;
    }

    @RequestMapping(value = "/{accountId:[\\d]+}", method = RequestMethod.GET)
    @ApiOperation(value = "Get the account information by the specified accountId.", httpMethod = "GET", produces = "application/json")
    public AccountDto getAccount(Authentication auth, @PathVariable("accountId") long accountId) {
        logger.info("GET /acct/accounts/{}, user: {}.", accountId, auth.getName());
        return DtoUtil.transform(AccountDto.class, accountService.getAccount(auth, accountId));
    }

    @RequestMapping(value = "/{accountId:[\\d]+}/users", method = RequestMethod.GET)
    @ApiOperation(value = "Get all the users who can operate the account with the specified accountId.", httpMethod = "GET", produces = "application/json")
    public CollectionResponse<UserAccountDto> getUsersByAccount(Authentication auth, @PathVariable("accountId") long accountId) {
        logger.info("GET /acct/accounts/{}/users, user: {}.", accountId, auth.getName());
        List<UserAccount> list = accountService.getUsersByAccountId(auth, accountId);
        return new CollectionResponse<>("user_accounts", null, DtoUtil.transformList(UserAccountDto.class, list, DateTimeHelper.DEFAULT_TIME_ZONE));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Get all the accounts contains the specified name.", httpMethod = "GET", produces = "application/json")
    public CollectionResponse<AccountDto> getAccountsByName(Authentication auth,
            @RequestParam("name") @Size(min = 3, max = 126, message = MSG_NAME_LEN) String name,
            @RequestParam(name = "offset", defaultValue = "0") @ValidOffset int offset,
            @RequestParam(name = "limit", defaultValue = "100") @ValidLimit int limit,
            @RequestParam(name = "sortBy", defaultValue = "accountId") @ValidSortBy(dto = AccountDto.class) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") @ValidOrder String sortOrder, HttpServletRequest request) {
        List<AccountDto> list = accountService.getAccountsByName(auth, name, offset, limit + 1, sortBy, sortOrder).stream()
                .map(a -> DtoUtil.transform(AccountDto.class, a)).collect(Collectors.toList());
        return CollectionResponseBuilder.withHttpServletRequest(request).withPageInfo(offset, limit, sortBy, sortOrder)
                .build("accounts", list);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Create new account.", httpMethod = "POST", produces = "application/json")
    public AccountDto createAccount(Authentication auth,
            @Validated(value = { Create.class, Default.class }) @RequestBody AccountDto req) {
        logger.info("POST /acct/accounts, user: {}.", auth.getName());
        req.setAccountId(null);
        Account account = DtoUtil.transform(Account.class, req);
        Account created = accountService.createAccount(auth, account);
        return DtoUtil.transform(AccountDto.class, created);
    }

    @RequestMapping(value = "/{accountId:[\\d]+}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update an account.", httpMethod = "PUT", produces = "application/json")
    public AccountDto updateAccount(Authentication auth, @PathVariable("accountId") long accountId,
            @Valid @RequestBody AccountDto req) {
        logger.info("PUT /acct/accounts/{}, user: {}.", accountId, auth.getName());
        ValidationUtil.validateId(req, accountId);

        Account account = DtoUtil.transform(Account.class, req);
        account.setAccountId(accountId);
        Account updated = accountService.updateAccount(auth, account);
        return DtoUtil.transform(AccountDto.class, updated);
    }

    @RequestMapping(value = "/{accountId:[\\d]+}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete an account.", httpMethod = "DELETE", produces = "application/json")
    public GenericResponse<String> deleteAccount(Authentication auth, @PathVariable("accountId") long accountId) {
        boolean deleted = accountService.deleteAccount(auth, accountId);
        if (deleted) {
            return new GenericResponse<String>("Account deleted.");
        } else {
            return new GenericResponse<String>("Account not found.");
        }
    }

    @RequestMapping(value = "/{accountId:[\\d]+}/activate", method = RequestMethod.PATCH)
    @ApiOperation(value = "Activate an account.", httpMethod = "PATCH", produces = "application/json")
    public GenericResponse<String> activateAccount(Authentication auth, @PathVariable("accountId") long accountId) {
        boolean activated = accountService.activateAccount(auth, accountId);
        if (activated) {
            return new GenericResponse<String>("Account activated.");
        } else {
            return new GenericResponse<String>("Account not found.");
        }
    }

    @RequestMapping(value = "/{accountId:[\\d]+}/deactivate", method = RequestMethod.PATCH)
    @ApiOperation(value = "Deactivate an account.", httpMethod = "PATCH", produces = "application/json")
    public GenericResponse<String> deactivateAccount(Authentication auth, @PathVariable("accountId") long accountId) {
        boolean deactivated = accountService.deactivateAccount(auth, accountId);
        if (deactivated) {
            return new GenericResponse<String>("Account deactivated.");
        } else {
            return new GenericResponse<String>("Account not found.");
        }
    }

}
