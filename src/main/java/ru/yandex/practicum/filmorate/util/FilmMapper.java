package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.util.Set;

public class FilmMapper {
    public static Film map(FilmDTO dto, Integer id, Set<Integer> likes) {
        return Film.builder()
                .id(id)
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(Duration.ofMinutes(dto.getDuration()))
                .likesUserId(likes)
                .build();
    }

    public static Film map(FilmDTO.WithId dto, Set<Integer> likes) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(Duration.ofMinutes(dto.getDuration()))
                .likesUserId(likes)
                .build();
    }
}
