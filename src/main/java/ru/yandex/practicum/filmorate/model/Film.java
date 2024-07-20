package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.yandex.practicum.filmorate.validator.OlderThen;

import java.time.Duration;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class Film {
    @NonNull
    @NotNull(message = "id is mandatory field")
    Integer id;

    @NonNull
    @NotBlank(message = "name is mandatory field")
    String name;

    @Size(max = 200, message = "description length should be less or equal then 200")
    String description;

    @NonNull
    @OlderThen(
            value = "1895-12-28",
            message = "releaseDate must be older then 1895-12-28",
            NotNull = true
    )
    LocalDate releaseDate;

    @NonNull
    @NotNull(message = "duration is mandatory")
    Duration duration;
}
