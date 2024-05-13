package com.dallinjohnson.projectmanager.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateOrderValidator implements ConstraintValidator<ValidDateOrder, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(ValidDateOrder constraintAnnotation) {
        startDateField = constraintAnnotation.startDate();
        endDateField = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        try {
            Field startDate = dto.getClass().getDeclaredField(startDateField);
            Field endDate = dto.getClass().getDeclaredField(endDateField);
            startDate.setAccessible(true);
            endDate.setAccessible(true);

            // Get the actual values of startDate and endDate from the DTO instance
            Object startDateValue = startDate.get(dto);
            Object endDateValue = endDate.get(dto);

            // Perform the date order validation
            if (startDateValue instanceof LocalDate && endDateValue instanceof LocalDate) {
                LocalDate start = (LocalDate) startDateValue;
                LocalDate end = (LocalDate) endDateValue;
                return start == null || end == null || start.isBefore(end);
            }
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            // Handle exceptions if necessary
        }
        return false;
    }
}