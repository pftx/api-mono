/**
 * DtoUtil.java
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
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.niolex.commons.collection.CollectionUtil;
import org.apache.niolex.commons.compress.JacksonUtil;
import org.apache.niolex.commons.reflect.FieldUtil;

import com.x.api.common.dto.BaseDto;
import com.x.api.common.dto.Timezone;
import com.x.api.common.dto.annotation.IgnoreInput;
import com.x.api.common.exception.BadRequestException;
import com.x.api.common.exception.ServiceException;
import com.x.api.common.helper.DateTimeHelper;
import com.x.api.common.model.BaseModel;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 14, 2017
 */
public class DtoUtil {

    public static <Dto, Mod> List<Dto> transformList(Class<Dto> cls, List<Mod> model, String timezone) {
        if (CollectionUtil.isEmpty(model)) {
            return Collections.emptyList();
        }
        Function<Mod, Dto> func = m -> toDto(cls, m, timezone);
        return model.stream().map(func).collect(Collectors.toList());
    }

    public static <Dto extends BaseDto, Mod extends BaseModel> Dto transform(Class<Dto> cls, Mod model) {
        return toDto(cls, model,
                model instanceof Timezone ? ((Timezone) model).getTimezone() : DateTimeHelper.DEFAULT_TIME_ZONE);
    }

    public static <Dto, Mod> Dto toDto(Class<Dto> cls, Mod model, String timezone) {
        try {
            DateTimeHelper.setTimezoneContext(timezone);
            return directCopy(cls, model);
        } finally {
            if (timezone != null) {
                DateTimeHelper.setTimezoneContext(null);
            }
        }
    }

    public static <Dto extends BaseDto, Mod extends BaseModel> Mod transform(Class<Mod> cls, Dto dto) {
        return toModel(cls, dto, dto instanceof Timezone ? ((Timezone) dto).getTimezone() : DateTimeHelper.DEFAULT_TIME_ZONE);
    }

    /**
     * When transform Dto to Model, we ignore all the fields marked with annotation {@link IgnoreInput}.
     * 
     * @param cls
     *            the model class
     * @param dto
     *            the dto bean
     * @param timezone
     *            the time zone used to parse date and time
     * @return the transformed model object
     */
    public static <Dto, Mod> Mod toModel(Class<Mod> cls, Dto dto, String timezone) {
        List<Field> allFields = FieldUtil.getAllFields(dto.getClass());

        try {
            DateTimeHelper.setTimezoneContext(timezone);
            for (Field fi : allFields) {
                if (!fi.isAnnotationPresent(IgnoreInput.class)) {
                    continue;
                }

                fi.setAccessible(true);
                fi.set(dto, null);
            }

            Mod model = directCopy(cls, dto);
            return model;
        } catch (Exception e) {
            BadRequestException ex = new BadRequestException("Failed to parse request body.", e);
            ex.addExtraInfo("debug_info", e.getMessage());
            throw ex;
        } finally {
            if (timezone != null) {
                DateTimeHelper.setTimezoneContext(null);
            }
        }
    }

    public static <T1, T2> T2 directCopy(Class<T2> cls, T1 from) {
        try {
            String s = JacksonUtil.obj2Str(from);
            return JacksonUtil.str2Obj(s, cls);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
