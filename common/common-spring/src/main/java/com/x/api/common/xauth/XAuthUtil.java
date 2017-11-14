/**
 * XAuthUtil.java
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
package com.x.api.common.xauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.base.Preconditions;
import com.x.api.common.exception.AuthorizationException;
import com.x.api.common.util.Constants;
import com.x.api.common.xauth.token.SecuredXToken;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
public class XAuthUtil {

    public static void checkCanOperate(Authentication auth, long accountId) {
        if (!canOperate(auth, accountId)) {
            throw AuthorizationException.noPermissionToOperate(Constants.TYPE_ACCOUNT, accountId);
        }
    }

    public static boolean canOperate(Authentication auth, long accountId) {
        return canSuper(auth) || hasAccount(auth, accountId);
    }

    public static boolean canSuper(Authentication auth) {
        return hasPerm(auth, Constants.PERM_SUPER_LOGIN);
    }

    public static boolean hasAccount(Authentication auth, long accountId) {
        if (auth instanceof SecuredXToken) {
            SecuredXToken sxToken = (SecuredXToken) auth;
            return sxToken.getPrincipal().getAccountList().stream().filter(a -> a.getAccountId() == accountId).findAny()
                    .isPresent();
        }
        return false;
    }

    public static boolean hasPerm(Authentication auth, String perm) {
        Preconditions.checkNotNull(auth);
        Preconditions.checkNotNull(perm);
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(s -> s.equalsIgnoreCase(perm))
                .findAny().isPresent();
    }

}
