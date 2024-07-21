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
    @NotBlank(message = "name is mandatory field")
    String name;

    @Size(max = 200, message = "description length should be less or equal then 200")
    String description;

    @NotNull(message = "releaseDate is mandatory field")
    @OlderThen(value = "1895-12-28", message = "releaseDate must be older then 1895-12-28")
    LocalDate releaseDate;

    @NotNull(message = "duration is mandatory")
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
