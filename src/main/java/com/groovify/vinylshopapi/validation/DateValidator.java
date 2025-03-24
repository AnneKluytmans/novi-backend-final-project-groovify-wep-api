package com.groovify.vinylshopapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<ValidDate, LocalDate> {

    private LocalDate minDate;
    private LocalDate maxDate;
    private boolean mustBePast;
    private boolean mustBeFuture;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        minDate = constraintAnnotation.min().isEmpty() ? null : ValidationUtils.parseValidationDate(constraintAnnotation.min());
        maxDate = constraintAnnotation.max().isEmpty() ? null : ValidationUtils.parseValidationDate(constraintAnnotation.max());
        mustBePast = constraintAnnotation.mustBePast();
        mustBeFuture = constraintAnnotation.mustBeFuture();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }

        if (minDate != null && date.isBefore(minDate)) {
            return false;
        }
        if (maxDate != null && date.isAfter(maxDate)) {
            return false;
        }

        if (mustBePast && !date.isBefore(LocalDate.now())) {
            return false;
        }
        if (mustBeFuture && !date.isAfter(LocalDate.now())) {
            return false;
        }

        return true;
    }
}

