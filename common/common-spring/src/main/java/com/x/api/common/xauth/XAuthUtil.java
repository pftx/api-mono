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
 * The XAuth is a very simple authentication method used internally. It just like JWT, save everything into the token
 * itself so there is no need to query auth-service to find user details. <br>
 * We query auth-service only once at gateway, then encode token details into X-token and send it to back-end
 * micro-service servers.
 * 
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
public class XAuthUtil {

    /**
     * Check whether the authentication can operate the specified account.
     * 
     * @param auth the authentication holder
     * @param accountId the account Id
     * @throws AuthorizationException if can not operate
     */
    public static void checkCanOperate(Authentication auth, long accountId) {
        if (!canOperate(auth, accountId)) {
            throw AuthorizationException.noPermissionToOperate(Constants.TYPE_ACCOUNT, accountId);
        }
    }

    /**
     * Check whether the authentication can operate the specified account.
     * 
     * @param auth the authentication holder
     * @param accountId the account Id
     * @return true if can operate
     */
    public static boolean canOperate(Authentication auth, long accountId) {
        return hasAccountPerm(auth, accountId, Constants.PERM_WRITE);
    }

    /**
     * Check whether the authentication can read the specified account.
     * 
     * @param auth the authentication holder
     * @param accountId the account Id
     * @throws AuthorizationException if can not read
     */
    public static void checkCanRead(Authentication auth, long accountId) {
        if (!canRead(auth, accountId)) {
            throw AuthorizationException.noPermissionToRead(Constants.TYPE_ACCOUNT, accountId);
        }
    }

    /**
     * Check whether the authentication can read the specified account.
     * 
     * @param auth the authentication holder
     * @param accountId the account Id
     * @return true if can read
     */
    public static boolean canRead(Authentication auth, long accountId) {
        return canSuper(auth) || hasAccountPerm(auth, accountId, Constants.PERM_READ);
    }

    /**
     * Check whether the authentication can super into other accounts.
     * 
     * @param auth the authentication holder
     * @return true if can super
     */
    public static boolean canSuper(Authentication auth) {
        return hasPerm(auth, Constants.PERM_SUPER_LOGIN);
    }

    /**
     * Check whether the authentication has the specified permission.
     * 
     * @param auth the authentication holder
     * @param perm the permission
     * @return true if has the specified permission
     */
    public static boolean hasPerm(Authentication auth, String perm) {
        Preconditions.checkNotNull(auth);
        Preconditions.checkNotNull(perm);
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(s -> s.equalsIgnoreCase(perm))
                .findAny().isPresent();
    }

    /**
     * Check whether the authentication has the specified account permission.
     * 
     * @param auth the authentication holder
     * @param accountId the account Id
     * @param perm the permission
     * @return true if has the specified permission
     */
    public static boolean hasAccountPerm(Authentication auth, long accountId, String perm) {
        if (auth instanceof SecuredXToken) {
            SecuredXToken xToken = (SecuredXToken) auth;
            return xToken.getPrincipal().getOpAccountId() == accountId && hasPerm(auth, perm);
        }
        return false;
    }

}
