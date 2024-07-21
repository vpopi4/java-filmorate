package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validator.NotBlankIfNotNull;
import ru.yandex.practicum.filmorate.validator.OlderThen;

import java.time.Duration;
import java.time.LocalDate;

@Value
@Builder
public class FilmPatchDTO {
    @NotBlankIfNotNull(message = "name must be not blank if not null")
    String name;

    @Size(max = 200, message = "description length should be less or equal then 200")
    String description;

    @OlderThen(
            value = "1895-12-28",
            message = "releaseDate must be older then 1895-12-28",
            NotNull = false
    )
    LocalDate releaseDate;

    @Positive
    Integer durationInMilliseconds;

    public Film toFilm(Integer id) {
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(Duration.ofMillis(durationInMilliseconds))
                .build();
    }
}

