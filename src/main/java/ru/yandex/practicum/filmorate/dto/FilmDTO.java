package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.validator.OlderThen;

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
    Integer duration;

    @Value
    @Builder
    public static class WithId {
        @NotNull
        Integer id;

        @NotBlank
        String name;

        @Size(max = 200)
        String description;

        @NotNull
        @OlderThen(value = "1895-12-28", message = "must be older then 1895-12-28")
        LocalDate releaseDate;

        @NotNull
        @Positive
        Integer duration;
    }
}
