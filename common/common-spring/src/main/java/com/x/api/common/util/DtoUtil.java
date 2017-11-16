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

import java.text.DateFormat;

import org.apache.niolex.commons.compress.JacksonUtil;

import com.x.api.common.dto.BaseDto;
import com.x.api.common.dto.Timezone;
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

    public static <Dto extends BaseDto, Mod extends BaseModel> Dto transform(Class<Dto> cls, Mod model) {
        return transform(cls, model,
                model instanceof Timezone ? ((Timezone) model).getTimezone() : DateTimeHelper.DEFAULT_TIME_ZONE);
    }

    public static <Dto extends BaseDto, Mod extends BaseModel> Dto transform(Class<Dto> cls, Mod model,
            String timezone) {
        Dto dto = directCopy(cls, model);
        DateFormat formatter = DateTimeHelper.getFormatter(timezone);
        if (model.getCreated() != null) {
            dto.setCreated(formatter.format(model.getCreated()));
        }
        if (model.getModified() != null) {
            dto.setModified(formatter.format(model.getModified()));
        }
        return dto;
    }

    public static <Dto extends BaseDto, Mod extends BaseModel> Mod transform(Class<Mod> cls, Dto dto) {
        return transform(cls, dto,
                dto instanceof Timezone ? ((Timezone) dto).getTimezone() : DateTimeHelper.DEFAULT_TIME_ZONE);
    }

    public static <Dto extends BaseDto, Mod extends BaseModel> Mod transform(Class<Mod> cls, Dto dto,
            String timezone) {
        try {
            dto.setCreated(null);
            dto.setModified(null);
            Mod model = directCopy(cls, dto);
            return model;
        } catch (Exception e) {
            throw new BadRequestException("Failed to transform Dto to Model.", e);
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
