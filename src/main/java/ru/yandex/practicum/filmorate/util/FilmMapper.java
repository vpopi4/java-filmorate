package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;

public class FilmMapper {
    public static Film map(FilmDTO dto, Integer id) {
        return Film.builder()
                .id(id)
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(Duration.ofMinutes(dto.getDuration()))
                .build();
    }

    public static Film map(FilmDTO.WithId dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(Duration.ofMinutes(dto.getDuration()))
                .build();
    }
}
