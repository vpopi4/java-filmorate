package ru.yandex.practicum.filmorate.mapper;

import org.springframework.lang.Nullable;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.Duration;
import java.util.List;

public class FilmDtoMapper {
    public static Film map(FilmDTO.Request.Create dto,
                           Integer id,
                           @Nullable MpaRating rating,
                           @Nullable List<Genre> genres) {
        return Film.builder()
                .id(id)
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(Duration.ofMinutes(dto.getDuration()))
                .mpa(rating)
                .genres(genres)
                .build();
    }

    public static Film map(FilmDTO.Request.Update dto,
                           @Nullable MpaRating rating,
                           @Nullable List<Genre> genres) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(Duration.ofMinutes(dto.getDuration()))
                .mpa(rating)
                .genres(genres)
                .build();
    }
}
