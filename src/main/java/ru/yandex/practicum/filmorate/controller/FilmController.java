package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmDTO;
import ru.yandex.practicum.filmorate.model.FilmPatchDTO;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private int seq;
    private HashMap<Integer, Film> films;

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody FilmDTO data) {
        Film film = data.toFilm(getNextId());

        films.put(film.getId(), film);

        return film;
    }

    @PutMapping("/{id}")
    public Film updateOrCreate(@PathVariable Integer id,
                               @Valid @RequestBody FilmDTO data) {
        Integer checkedId = films.containsKey(id) ? id : getNextId();

        Film film = data.toFilm(checkedId);

        films.put(film.getId(), film);

        return film;
    }

    @PatchMapping("/{id}")
    public Film update(@PathVariable Integer id,
                       @Valid @RequestBody FilmPatchDTO dto) {
        Film existingFilm = films.get(id);

        if (existingFilm == null) {
            throw new NotFoundException("Film with id=" + id + " not found");
        }

        Film.FilmBuilder builder = existingFilm.toBuilder();

        if (dto.getName() != null) {
            builder.name(dto.getName());
        }

        if (dto.getDescription() != null) {
            builder.description(dto.getDescription());
        }

        if (dto.getReleaseDate() != null) {
            builder.releaseDate(dto.getReleaseDate());
        }

        if (dto.getDurationInMilliseconds() != null) {
            builder.duration(Duration.ofMillis(dto.getDurationInMilliseconds()));
        }

        Film updatedFilm = builder.build();

        films.put(id, updatedFilm);

        return updatedFilm;
    }

    private Integer getNextId() {
        return ++seq;
    }
}

