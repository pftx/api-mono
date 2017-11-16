/**
 * Constants.java
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
package com.x.api.common.util;

import java.math.BigDecimal;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 6, 2017
 */
public interface Constants {

    String HEADER_X_TOKEN = "X-Token";
    String HEADER_X_UID = "X-Uid";
    String HEADER_X_ERR_MSG = "X-Err-Msg";
    String HEADER_X_FORWARD = "X-Forwarded-For";

    String MSG_SERVICE_UNAVAILABLE = "Service temporarily unavailable, please try again later.";
    String MSG_INTERNAL_ERROR =
            "We encountered an internal error. Please contact our client services team or try again later.";
    String MSG_NO_ACCESS_TOKEN = "Access token is required to access this resource.";
    String MSG_BAD_ACCESS_TOKEN = "Invalid access token.";
    String MSG_BAD_X_TOKEN = "Invalid X-Token.";
    String MSG_BAD_REQUEST_VALIDATION = "Your request failed to pass validation.";
    String MSG_NO_PERMISSION = "You are not authorized to access this endpoint.";
    String MSG_NO_PERM_TO_OP = "You are not authorized to access the %s with Id %d.";
    String MSG_NOT_FOUND = "The %s with Id %d not found.";

    String PERM_SUPER_LOGIN = "super_into";
    String PERM_ADMIN_METRICS = "admin_metrics";
    String PERM_ACCT_ADMIN = "account_admin";
    String PERM_WRITE = "account_write";
    String PERM_READ = "account_read";

    String TYPE_ACCOUNT = "account";

    int INT_MILLION = 1000000;

    BigDecimal BD_MILLION = new BigDecimal(INT_MILLION);

}
