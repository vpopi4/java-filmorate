package ru.yandex.practicum.filmorate.util;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BaseResponseBody<P> {
    Integer status;
    String message;
    P payload;
}
