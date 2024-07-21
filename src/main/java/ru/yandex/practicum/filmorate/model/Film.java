package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class Film {
    @NonNull
    Integer id;

    @NonNull
    String name;

    String description;

    @NonNull
    LocalDate releaseDate;

    @NonNull
    Duration duration;
}
