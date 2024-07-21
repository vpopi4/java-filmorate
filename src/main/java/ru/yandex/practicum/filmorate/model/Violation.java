package ru.yandex.practicum.filmorate.model;

import lombok.Value;

@Value
public class Violation {
    String fieldName;
    String message;
}
