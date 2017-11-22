/**
 * MyNotNullValidator.java
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
package com.x.api.common.validation.validator;

import javax.validation.ConstraintValidatorContext;

import com.x.api.common.validation.annotation.MyNotNull;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 22, 2017
 */
public class MyNotNullValidator extends AllowNullConstraintValidator<MyNotNull, Object> {

    /**
     * This is the override of super method.
     * @see com.x.api.common.validation.validator.AllowNullConstraintValidator#isValidNonNull(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValidNonNull(Object value, ConstraintValidatorContext context) {
        return false;
    }

    /**
     * This is the override of super method.
     * @see com.x.api.common.validation.validator.AllowNullConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            generateErrorContext(context);
            return false;
        }
        return true;
    }

}
