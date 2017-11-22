/**
 * AllowNullConstraintValidator.java
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

import java.lang.annotation.Annotation;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.niolex.commons.reflect.FieldUtil;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

/**
 * Allow null value as valid.
 * 
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 22, 2017
 */
public abstract class AllowNullConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

    /**
     * This is the override of super method.
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(A constraintAnnotation) {}

    /**
     * This is the override of super method.
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return isValidNonNull(value, context);
    }

    protected void generateErrorContext(ConstraintValidatorContext context) {
        if (context instanceof HibernateConstraintValidatorContext) {
            HibernateConstraintValidatorContext h = (HibernateConstraintValidatorContext) context;
            Object basePath = FieldUtil.getValue(h, "basePath");
            h.addExpressionVariable("propertyPath", Objects.toString(basePath));
            extraErrorContext(h);
        }
    }

    protected void extraErrorContext(HibernateConstraintValidatorContext hContext) {}

    public abstract boolean isValidNonNull(T value, ConstraintValidatorContext context);

}
