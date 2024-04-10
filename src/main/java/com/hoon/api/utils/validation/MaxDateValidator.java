package com.hoon.api.utils.validation;

import com.hoon.api.utils.validation.annotation.MaxDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MaxDateValidator implements ConstraintValidator<MaxDate, String> {

    @Override
    public void initialize(MaxDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        int oneYearAgoInt = Integer.parseInt(oneYearAgo.format(DateTimeFormatter.BASIC_ISO_DATE));

        return value == null || Integer.parseInt(value) >= oneYearAgoInt;

    }
}
