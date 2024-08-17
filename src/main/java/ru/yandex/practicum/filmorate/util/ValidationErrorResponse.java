package ru.yandex.practicum.filmorate.util;

import lombok.Value;

import java.util.List;

@Value
public class ValidationErrorResponse {
    List<Violation> violations;
}
