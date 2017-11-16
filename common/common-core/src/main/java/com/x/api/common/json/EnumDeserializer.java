/**
 * EnumDeserializer.java
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 16, 2017
 */
public class EnumDeserializer<T extends Enum<T>> extends StdDeserializer<T> implements ContextualDeserializer {

    private static final long serialVersionUID = -1056361915012086193L;

    public EnumDeserializer() {
        this(null);
    }

    public EnumDeserializer(JavaType vc) {
        super(vc);
    }

    /**
     * This is the override of super method.
     * 
     * @see com.fasterxml.jackson.databind.deser.ContextualDeserializer#createContextual(com.fasterxml.jackson.databind.DeserializationContext,
     *      com.fasterxml.jackson.databind.BeanProperty)
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        return new EnumDeserializer<T>(property.getType());
    }

    /**
     * This is the override of super method.
     * 
     * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
     *      com.fasterxml.jackson.databind.DeserializationContext)
     */
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        @SuppressWarnings("unchecked")
        T[] constants = (T[]) _valueClass.getEnumConstants();
        String value = p.getValueAsString();
        for (T co : constants) {
            if (co.name().equalsIgnoreCase(value)) {
                return co;
            }
        }
        return null;
    }

}
