/**
 * DateTimeSerializer.java
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
package com.x.api.common.json;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.x.api.common.helper.DateTimeHelper;

/**
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2017年11月28日
 */
public class DateTimeSerializer extends StdSerializer<Date> {

    private static final long serialVersionUID = 1L;

    protected DateTimeSerializer() {
        super(Date.class);
    }

    /**
     * This is the override of super method.
     * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
     */
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(DateTimeHelper.getContextFormatter().format(value));
        }
    }

    /**
     * This is the override of super method.
     * 
     * @see com.fasterxml.jackson.databind.JsonSerializer#serializeWithType(java.lang.Object,
     *      com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider,
     *      com.fasterxml.jackson.databind.jsontype.TypeSerializer)
     */
    @Override
    public void serializeWithType(Date value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(DateTimeHelper.getFormatter(DateTimeHelper.DEFAULT_TIME_ZONE).format(value));
        }
    }

}
