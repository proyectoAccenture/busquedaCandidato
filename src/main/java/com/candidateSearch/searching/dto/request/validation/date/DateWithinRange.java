package com.candidateSearch.searching.dto.request.validation.date;

import jakarta.validation.Payload;
import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateWithinRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateWithinRange {
    String message() default "The date must be within the last 3 months and cannot be in the future.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
