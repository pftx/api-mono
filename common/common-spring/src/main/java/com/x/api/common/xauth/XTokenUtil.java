/**
 * XTokenUtil.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.niolex.commons.codec.SHAUtil;
import org.apache.niolex.commons.collection.CollectionUtil;
import org.apache.niolex.commons.util.DateTimeUtil;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.x.api.common.util.Constants;
import com.x.api.common.xauth.token.SecuredXToken;

/**
 * XTokenUtil, encode and decode X-Token.
 * 
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Oct 19, 2017
 */
public class XTokenUtil {

    private static final String TMP_MAP =
            "vAH3n)M=|J,_7dW@E;jh{b`]yfXlU9</c-L1Va.i?Z0R^eB:PQO>SDY6Nr5xp+GCTukoqFmwI8tsK2z}4[g~";
    private static char[] codeMap = new char[128];
    private static final char FIELD_SEP = ',';
    private static final char ATTRI_SEP = '|';

    static {
        System.arraycopy(TMP_MAP.toCharArray(), 0, codeMap, '+', 84);
    }

    public static SecuredXToken decodeToken(String rawToken, String secret) {
        String xToken = plainStr(rawToken);
        List<String> list = Splitter.on(FIELD_SEP).splitToList(xToken);
        if (list.size() != 7) {
            throw new IllegalArgumentException("Invalid X-Token: " + rawToken);
        }
        String enc = SHAUtil.sha1(xToken.substring(0, xToken.lastIndexOf(FIELD_SEP)), secret);
        String credential = list.get(6);
        if (!enc.startsWith(credential)) {
            throw new IllegalArgumentException("Bad X-Token: " + rawToken);
        }

        long startTime = toLong(list.get(5)) << 4;
        if (System.currentTimeMillis() - startTime > DateTimeUtil.HOUR * 2) {
            throw new IllegalArgumentException("X-Token expired: " + rawToken);
        }

        XTokenPrincipal principal = new XTokenPrincipal();
        principal.setUserName(list.get(0));
        principal.setUserId(toLong(list.get(1)));
        principal.setOpUserId(toLong(list.get(2)));
        principal.setOpAccountId(toLong(list.get(3)));
        principal.setPermissionList(Splitter.on(ATTRI_SEP).splitToList(list.get(4)));
        return new SecuredXToken(principal, rawToken);
    }

    public static String encodeToken(SecuredXToken token, String secret) {
        StringBuilder sb = new StringBuilder();
        XTokenPrincipal principal = token.getPrincipal();

        sb.append(principal.getUserName()).append(FIELD_SEP);
        sb.append(toStr(principal.getUserId())).append(FIELD_SEP);
        sb.append(toStr(principal.getOpUserId())).append(FIELD_SEP);
        sb.append(toStr(principal.getOpAccountId())).append(FIELD_SEP);
        sb.append(Joiner.on(ATTRI_SEP).join(calculatePermission(principal))).append(FIELD_SEP);
        sb.append(toStr(System.currentTimeMillis() >> 4));
        String enc = SHAUtil.sha1(sb.toString(), secret).substring(0, 6);
        sb.append(FIELD_SEP).append(enc);
        return secureStr(sb.toString());
    }

    public static List<String> calculatePermission(XTokenPrincipal principal) {
        List<AccountInfo> accountList = principal.getAccountList();
        if (CollectionUtil.isEmpty(accountList)) {
            return principal.getPermissionList();
        }

        AccountInfo info = principal.getAccountList().stream()
                .filter(i -> i.getAccountId().equals(principal.getOpAccountId())).findFirst().orElse(null);

        List<String> accountPerm = new ArrayList<>();
        if (info != null) {
            accountPerm.addAll(info.getPermissionList());
        } else {
            accountPerm.add(Constants.PERM_READ);
        }

        List<String> part1Perm = principal.getPermissionList().stream()
                .filter(s -> !s.startsWith("account_")).collect(Collectors.toList());
        List<String> part2Perm = principal.getPermissionList().stream()
                .filter(s -> accountPerm.contains(s)).collect(Collectors.toList());
        part1Perm.addAll(part2Perm);

        return part1Perm;
    }

    private static String toStr(Long l) {
        return l == null ? "" : Long.toString(l, 32);
    }

    private static Long toLong(String s) {
        return s.length() == 0 ? null : Long.parseLong(s, 32);
    }

    public static String secureStr(String str) {
        char[] cs = str.toCharArray();
        for (int i = 0; i < cs.length; ++i) {
            cs[i] = codeMap[cs[i]];
        }
        return new String(cs);
    }

    public static String plainStr(String str) {
        char[] cs = str.toCharArray();
        for (int i = 0; i < cs.length; ++i) {
            for (char c = '+'; c < 127; ++c) {
                if (cs[i] == codeMap[c]) {
                    cs[i] = c;
                    break;
                }
            }
        }
        return new String(cs);
    }

}
