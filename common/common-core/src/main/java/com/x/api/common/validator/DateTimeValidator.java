/**
 * DateTimeValidator.java
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

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import com.x.api.common.helper.DateTimeHelper;
import com.x.api.common.validator.annotation.ValidDateTime;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 17, 2017
 */
public class DateTimeValidator implements ConstraintValidator<ValidDateTime, String> {

    /**
     * This is the override of super method.
     * 
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(ValidDateTime constraintAnnotation) {}

    private void generateErrorContext(ConstraintValidatorContext context) {
        if (context instanceof HibernateConstraintValidatorContext) {
            HibernateConstraintValidatorContext h = (HibernateConstraintValidatorContext) context;
            Object basePath = FieldUtil.getValue(h, "basePath");
            h.addExpressionVariable("propertyPath", Objects.toString(basePath));
        }
    }

    /**
     * This is the override of super method.
     * 
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (DateTimeHelper.isValidDate(value)) {
            return true;
        } else {
            generateErrorContext(context);
            return false;
        }
    }

}
