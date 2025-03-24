package com.groovify.vinylshopapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
    String message() default "Date is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String min() default "";
    String max() default "";
    boolean mustBePast() default false;
    boolean mustBeFuture() default false;
}
