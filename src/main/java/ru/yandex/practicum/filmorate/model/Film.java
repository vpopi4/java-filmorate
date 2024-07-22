package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.yandex.practicum.filmorate.validator.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class Film {
    @NonNull Integer id;
    @NonNull String name;
    String description;
    @NonNull LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    @NonNull Duration duration;
}
