package com.hoon.api.utils.validation;



import com.hoon.api.utils.validation.annotation.DateTime;
import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeValidator implements ConstraintValidator<DateTime, String> {

    private String pattern;

    @Override
    public void initialize(DateTime constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try{
            if(StringUtils.hasText(value)) {
                LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern(this.pattern));
            }
        }catch (Exception e) {
            return false;
        }
        return true;
    }
}
