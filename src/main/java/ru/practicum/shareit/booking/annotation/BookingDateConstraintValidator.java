package ru.practicum.shareit.booking.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BookingDateConstraintValidator implements ConstraintValidator<BookingDate, LocalDate> {
    @Override
    public void initialize(BookingDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) return false;
        return !localDate.isBefore(LocalDate.now());
    }
}
