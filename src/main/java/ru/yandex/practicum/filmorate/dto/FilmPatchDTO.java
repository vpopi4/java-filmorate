package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validator.NotBlankIfNotNull;
import ru.yandex.practicum.filmorate.validator.OlderThen;

import java.time.LocalDate;

@Value
@Builder
public class FilmPatchDTO {
    @NotBlankIfNotNull(message = "name must be not blank if not null")
    String name;

    @Size(max = 200)
    String description;

    @OlderThen(
            value = "1895-12-28",
            message = "must be older then 1895-12-28",
            NotNull = false
    )
    LocalDate releaseDate;

    @Positive
    Integer duration;
}

