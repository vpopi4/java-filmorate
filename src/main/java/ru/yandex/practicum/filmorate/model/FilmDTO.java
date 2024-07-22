package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validator.OlderThen;

import java.time.Duration;
import java.time.LocalDate;

@Value
@Builder
public class FilmDTO {
    @NotBlank
    String name;

    @Size(max = 200)
    String description;

    @NotNull
    @OlderThen(value = "1895-12-28", message = "must be older then 1895-12-28")
    LocalDate releaseDate;

    @NotNull
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
