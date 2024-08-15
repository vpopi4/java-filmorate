package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.util.DurationSerializer;
import ru.yandex.practicum.filmorate.validator.OlderThen;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NewFilmDTO {
    private interface Name {
        String getName();
    }

    private interface Description {
        @Size(max = 200)
        String getDescription();
    }

    private interface ReleaseDate {
        @OlderThen(value = "1895-12-28", message = "must be older then 1895-12-28")
        LocalDate getReleaseDate();
    }

    private interface Duration {
        @Positive
        Integer getDuration();
    }



    public static class Request {
        @Value
        @Builder
        public static class Create implements Name, Description, ReleaseDate, NewFilmDTO.Duration {
            @NotBlank String name;
            String description;
            @NotNull LocalDate releaseDate;
            @NotNull Integer duration;
            List<Genre> genres;
        }
    }

    public static class Response {
        @Value
        @Builder
        public static class Create {
            Integer id;
            String name;
            String description;
            LocalDate releaseDate;
            @JsonSerialize(using = DurationSerializer.class)
            java.time.Duration duration;
            MpaRating mpa;
            List<Genre> genres;
            Set<Integer> likesUserId;
        }
    }
}
