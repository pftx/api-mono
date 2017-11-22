/**
 * ModelUtil.java
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

import java.lang.reflect.Field;
import java.util.List;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.x.api.common.model.annotation.NoUpdate;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 21, 2017
 */
public class ModelUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ModelUtil.class);

    public static <Mod> Mod prepareUpdate(Mod existing, Mod toBe) {
        Preconditions.checkNotNull(existing);
        Preconditions.checkNotNull(toBe);
        List<Field> allFields = FieldUtil.getAllFields(existing.getClass());

        for (Field fi : allFields) {
            if (fi.isAnnotationPresent(NoUpdate.class)) {
                continue;
            }

            fi.setAccessible(true);
            Object value = null;
            try {
                value = fi.get(toBe);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // Log the exception.
                LOG.error("Failed to get field value.", e);
            }
            if (value == null) {
                continue;
            }

            try {
                fi.set(existing, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // Log the exception.
                LOG.error("Failed to set field value.", e);
            }
        }
        return existing;
    }

}
