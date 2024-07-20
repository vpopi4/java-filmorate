package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class OlderThanValidator implements ConstraintValidator<OlderThen, LocalDate> {
    private LocalDate thresholdDate;
    private boolean NotNull;

    @Override
    public void initialize(OlderThen constraintAnnotation) {
        this.thresholdDate = LocalDate.parse(constraintAnnotation.value());
        this.NotNull = constraintAnnotation.NotNull();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return !NotNull;
        }

        return !value.isBefore(thresholdDate);
    }
}
