package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.util.DurationSerializer;
import ru.yandex.practicum.filmorate.validator.NotBlankIfNotNull;
import ru.yandex.practicum.filmorate.validator.OlderThen;

import java.time.LocalDate;
import java.util.List;

public class FilmDTO {
    private interface Id {
        Integer getId();
    }

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
        public static class Create implements Name, Description, ReleaseDate, FilmDTO.Duration {
            @NotBlank String name;
            String description;
            @NotNull LocalDate releaseDate;
            @NotNull Integer duration;
            List<GenreDTO> genres;
            MpaRatingDTO mpa;
        }

        @Value
        @Builder
        public static class Update implements Id, Name, Description, ReleaseDate, FilmDTO.Duration {
            @NotNull Integer id;
            @NotBlank String name;
            String description;
            @NotNull LocalDate releaseDate;
            @NotNull Integer duration;
            List<GenreDTO> genres;
            MpaRatingDTO mpa;
        }

        @Value
        @Builder
        public static class UpdatePartially implements Name, Description, FilmDTO.Duration {
            @NotBlankIfNotNull
            String name;
            @OlderThen(
                    value = "1895-12-28",
                    message = "must be older then 1895-12-28",
                    NotNull = false
            )
            LocalDate releaseDate;
            String description;
            Integer duration;
            List<GenreDTO> genres;
            MpaRatingDTO mpa;
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
        }
    }
}
