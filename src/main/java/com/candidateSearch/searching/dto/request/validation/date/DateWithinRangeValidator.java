package com.candidateSearch.searching.dto.request.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateWithinRangeValidator implements ConstraintValidator<DateWithinRange, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) return true;

        LocalDate now = LocalDate.now();
        LocalDate threeMonthsAgo = now.minusMonths(3);

        return !date.isAfter(now) && !date.isBefore(threeMonthsAgo);
    }
}
