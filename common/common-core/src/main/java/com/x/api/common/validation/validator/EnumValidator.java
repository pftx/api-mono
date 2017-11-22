/**
 * EnumValidator.java
 *
 * Copyright 2017 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.x.api.common.validation.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import com.google.common.base.Joiner;
import com.x.api.common.validation.annotation.ValidEnum;

/**
 * @author <a href="mailto:pftx@live.com">Lex Xie</a>
 * @version 1.0.0
 * @since Nov 16, 2017
 */
public class EnumValidator extends AllowNullConstraintValidator<ValidEnum, String> {

    private List<String> supportedList;

    /**
     * This is the override of super method.
     * 
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        Class<?> targetType = constraintAnnotation.type();
        Enum<?>[] constants = (Enum<?>[]) targetType.getEnumConstants();
        if (constants == null) {
            supportedList = Arrays.stream(constraintAnnotation.supportedList()).collect(Collectors.toList());
        } else {
            supportedList = Arrays.stream(constants).map(Enum::name).collect(Collectors.toList());
        }
    }

    /**
     * This is the override of super method.
     * @see com.x.api.common.validation.validator.AllowNullConstraintValidator#extraErrorContext(org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext)
     */
    @Override
    protected void extraErrorContext(HibernateConstraintValidatorContext h) {
        h.addExpressionVariable("supported_list", Joiner.on(", ").join(supportedList));
    }

    /**
     * This is the override of super method.
     * 
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValidNonNull(String value, ConstraintValidatorContext context) {
        if (supportedList.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Enum class not set for validator, value = " + value)
                    .addConstraintViolation();
            return false;
        }

        Optional<String> optional = supportedList.stream().filter(s -> s.equalsIgnoreCase(value)).findAny();

        if (optional.isPresent()) {
            return true;
        } else {
            generateErrorContext(context);
            return false;
        }
    }

}
