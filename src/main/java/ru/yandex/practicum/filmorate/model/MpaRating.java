package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class MpaRating {
    Integer id;
    @NonNull String name;
    String description;
}
