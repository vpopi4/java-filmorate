package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.FilmPatchDTO;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films;
    private int seq;

    public FilmController() {
        films = new HashMap<>();
    }

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody FilmDTO data) {
        log.info("handling POST /films");
        log.debug("with body={}", data);

        Integer id = getNextId();

        log.debug("creating film with id={}", id);
        Film film = data.toFilm(id);

        return putFilm(film);
    }

    private Film putFilm(Film film) {
        log.debug("saving film");
        films.put(film.getId(), film);

        log.info("sending film");
        log.debug("film={}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody FilmDTO.WithId data) {
        log.info("handling PUT /films");
        log.debug("with body={}", data);

        Integer id = data.getId();

        if (!films.containsKey(id)) {
            throw new NotFoundException("film with id=" + id + " not found");
        }

        log.debug("creating film with id={}", id);
        Film film = data.toFilm(id);

        return putFilm(film);
    }

    @PatchMapping("/{id}")
    public Film updatePartially(@PathVariable Integer id,
                                @Valid @RequestBody FilmPatchDTO data) {
        log.info("handling PATCH /films/{}", id);
        log.debug("with body={}", data);

        log.debug("getting saved film");
        Film savedFilm = films.get(id);

        if (savedFilm == null) {
            throw new NotFoundException("Film with id=" + id + " not found");
        }

        Film.FilmBuilder builder = savedFilm.toBuilder();

        if (data.getName() != null) {
            log.debug("updating film.name");
            builder.name(data.getName());
        }

        if (data.getDescription() != null) {
            log.debug("updating film.description");
            builder.description(data.getDescription());
        }

        if (data.getReleaseDate() != null) {
            log.debug("updating film.releaseDate");
            builder.releaseDate(data.getReleaseDate());
        }

        if (data.getDurationInMilliseconds() != null) {
            log.debug("updating film.duration");
            builder.duration(Duration.ofMillis(data.getDurationInMilliseconds()));
        }

        Film updatedFilm = builder.build();

        log.debug("saving film");
        films.put(id, updatedFilm);

        log.info("sending film");
        log.debug("film={}", updatedFilm);
        return updatedFilm;
    }

    private Integer getNextId() {
        return ++seq;
    }
}

