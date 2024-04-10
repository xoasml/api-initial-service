package com.hoon.api.utils.validation;


import com.hoon.api.utils.validation.annotation.EnumPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EnumPatternValidator implements ConstraintValidator<EnumPattern, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(EnumPattern annotation) {
        try {
            pattern = Pattern.compile(annotation.regexp());
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("pattern regex is invalid", e);
        }
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        System.out.println(value.name());
        Matcher m = pattern.matcher(value.name());
        if (!m.matches()) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("Value " + value + " does not match pattern " + pattern)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
