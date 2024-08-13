package ru.yandex.practicum.filmorate.util;

import lombok.Value;

@Value
public class Violation {
    String fieldName;
    String message;
}
