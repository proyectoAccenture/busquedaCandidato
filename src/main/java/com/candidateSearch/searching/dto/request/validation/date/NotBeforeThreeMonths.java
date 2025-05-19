package com.candidateSearch.searching.dto.request.validation.date;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NotBeforeThreeMonthsValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBeforeThreeMonths {
    String message() default "The date cannot be more than 3 months in the past.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
