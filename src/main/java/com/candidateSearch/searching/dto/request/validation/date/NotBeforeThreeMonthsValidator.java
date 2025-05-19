package com.candidateSearch.searching.dto.request.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class NotBeforeThreeMonthsValidator implements ConstraintValidator<NotBeforeThreeMonths, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) return true;
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        return !date.isBefore(threeMonthsAgo);
    }
}
