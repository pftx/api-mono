/**
 * ExceptionUtil.java
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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 2, 2017
 */
public class ExceptionUtil {

    public static String getExceptionMessage(Throwable e) {
        Preconditions.checkNotNull(e);
        String msg = e.getMessage();
        if (msg == null && e.getCause() != null) {
            msg = e.getCause().getMessage();
        }
        if (msg == null) {
            String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getClass().getName());
            return "We encountered " + name.replaceAll("_", " ") + ".";
        }
        return msg;
    }

    public static HttpStatus findHttpStatus(Throwable e) {
        Preconditions.checkNotNull(e);
        Class<?> c = e.getClass();
        do {
            ResponseStatus annotation = c.getAnnotation(ResponseStatus.class);
            if (annotation != null) {
                return annotation.value();
            }
            c = c.getSuperclass();
        } while (c != null);

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
