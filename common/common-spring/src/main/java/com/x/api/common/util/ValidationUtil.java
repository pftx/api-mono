/**
 * ValidationUtil.java
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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.api.common.dto.annotation.Id;
import com.x.api.common.exception.ValidationException;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 21, 2017
 */
public class ValidationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationUtil.class);

    public static <Dto> void validateId(Dto dto, long id) {
        Arrays.stream(dto.getClass().getDeclaredFields()).filter(f -> f.isAnnotationPresent(Id.class)).findFirst()
                .ifPresent(f -> {
                    try {
                        f.setAccessible(true);
                        Object value = f.get(dto);
                        if (value != null && Long.parseLong(value.toString()) != id) {
                            throw ValidationException.consistency(f.getName(), "request body", "path");
                        }
                    } catch (NumberFormatException e) { // NOSONAR.
                        throw ValidationException.consistency(f.getName(), "request body", "path");
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        // Log the exception.
                        LOG.error("Failed to validate Id field: {}.", f.getName(), e);
                    }
                });

        Arrays.stream(dto.getClass().getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Id.class)).findFirst()
                .ifPresent(m -> {
                    String name = MiscUtil.getAttributeName(m.getName());
                    try {
                        Object value = m.invoke(dto);
                        if (value != null && Long.parseLong(value.toString()) != id) {
                            throw ValidationException.consistency(name, "request body", "path");
                        }
                    } catch (NumberFormatException e) { // NOSONAR.
                        throw ValidationException.consistency(name, "request body", "path");
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        // Log the exception.
                        LOG.error("Failed to validate Id method: {}.", name, e);
                    }
                });
    }

}
