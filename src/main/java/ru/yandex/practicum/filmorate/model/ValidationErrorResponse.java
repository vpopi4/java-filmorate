package ru.yandex.practicum.filmorate.model;

import lombok.Value;

import java.util.List;

@Value
public class ValidationErrorResponse {
    List<Violation> violations;
}
