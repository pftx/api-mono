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

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.x.api.account.dto.AccountDto;
import com.x.api.account.model.Account;
import com.x.api.account.service.AccountService;
import com.x.api.common.dto.GenericResponse;
import com.x.api.common.util.DtoUtil;

import io.swagger.annotations.ApiOperation;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 27, 2017
 */
@RestController
@RequestMapping(value = "/acct/accounts")
public class AccountController {

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

    @RequestMapping(value = "/{accountId:[\\d]+}", method = RequestMethod.GET)
    @ApiOperation(value = "Get the account information by the specified accountId.", httpMethod = "GET",
            produces = "application/json")
    public AccountDto getAccount(Authentication auth, @PathVariable("accountId") long accountId) {
        logger.info("GET /acct/accounts/{}, user: {}.", accountId, auth.getName());
        return DtoUtil.transform(AccountDto.class, accountService.getAccount(auth, accountId));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Create new account.", httpMethod = "POST",
            produces = "application/json")
    public AccountDto createAccount(Authentication auth, @Valid @RequestBody AccountDto req) {
        logger.info("POST /acct/accounts, user: {}.", auth.getName());
        req.setAccountId(null);
        Account account = DtoUtil.transform(Account.class, req);
        Account created = accountService.createAccount(auth, account);
        return DtoUtil.transform(AccountDto.class, created);
    }

}
