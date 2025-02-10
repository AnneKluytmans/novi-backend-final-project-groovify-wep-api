package com.groovify.vinylshopapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidator implements ConstraintValidator<ValidDate, LocalDate> {

    private LocalDate minDate;
    private LocalDate maxDate;
    private boolean mustBePast;
    private boolean mustBeFuture;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        if (!constraintAnnotation.min().isEmpty()) {
            try {
                minDate = LocalDate.parse(constraintAnnotation.min());
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid min date format in @ValidDate");
            }
        }

        if (!constraintAnnotation.max().isEmpty()) {
            if (constraintAnnotation.max().equals("now+1Y")) {
                maxDate = LocalDate.now().plusYears(1);
            } else {
                try {
                    maxDate = LocalDate.parse(constraintAnnotation.max());
                } catch (DateTimeParseException ex) {
                    throw new IllegalArgumentException("Invalid max date format in @ValidDate");
                }
            }
        }

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

