/**
 * MiscUtil.java
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

import java.beans.Introspector;

import com.google.common.base.CaseFormat;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 17, 2017
 */
public class MiscUtil {

    public static int HEADER_MAX = 125;

    public static String httpHeaderEncode(String msg) {
        if (msg == null) {
            return null;
        }

        if (msg.length() > HEADER_MAX) {
            msg = msg.substring(0, HEADER_MAX) + "...";
        }

        return msg.replaceAll("\r*\n", "^");
    }

    public static String sqlLike(String raw) {
        if (!raw.startsWith("%")) {
            raw = "%" + raw;
        }
        if (!raw.endsWith("%")) {
            raw = raw + "%";
        }
        return raw;
    }

    public static String fieldNameToDbColumn(String fieldName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }

    public static String getAttributeName(String methodName) {
        // Assume the method starts with either get or is.
        return Introspector.decapitalize(methodName.substring(methodName.startsWith("is") ? 2 : 3));
    }

}
