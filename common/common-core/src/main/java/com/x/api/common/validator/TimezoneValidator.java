/**
 * TimezoneValidator.java
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
package com.x.api.common.validator;

import java.util.TimeZone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.x.api.common.validator.annotation.ValidTimezone;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 16, 2017
 */
public class TimezoneValidator implements ConstraintValidator<ValidTimezone, String> {
    static final String DEFAULT = "GMT";

    /**
     * This is the override of super method.
     * 
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(ValidTimezone constraintAnnotation) {
    }

    /**
     * This is the override of super method.
     * 
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        TimeZone zone = TimeZone.getTimeZone(value);
        return !zone.getID().equals(DEFAULT) || DEFAULT.equalsIgnoreCase(value);
    }

}
