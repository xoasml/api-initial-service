package com.hoon.api.utils.validation.annotation;



import com.hoon.api.utils.validation.DateTimeValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {DateTimeValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface DateTime {

    /**
     * @return the error message template
     */
    String message() default "yyyyMMdd 형식이 아닙니다.";

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default { };

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default { };

    /**
     * @return the regular expression to match
     */
    String pattern() default "yyyyMMdd";
}
