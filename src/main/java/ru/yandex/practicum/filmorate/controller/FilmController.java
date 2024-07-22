package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmDTO;
import ru.yandex.practicum.filmorate.model.FilmPatchDTO;

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

        return putFilm(getNextId(), data);
    }

    private Film putFilm(Integer id, FilmDTO data) {
        log.debug("creating film with id={}", id);
        Film film = data.toFilm(id);

        log.debug("saving film");
        films.put(film.getId(), film);

        log.info("sending film");
        log.debug("film={}", film);
        return film;
    }

    @PutMapping("/{id}")
    public Film updateOrCreate(@PathVariable Integer id,
                               @Valid @RequestBody FilmDTO data) {
        log.info("handling PUT /films/{}", id);
        log.debug("with body={}", data);

        if (films.containsKey(id)) {
            return putFilm(id, data);
        } else {
            log.trace("film with id={} not found", id);
            return putFilm(getNextId(), data);
        }
    }

    @PatchMapping("/{id}")
    public Film update(@PathVariable Integer id,
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
            log.trace("updating film.name");
            builder.name(data.getName());
        }

        if (data.getDescription() != null) {
            log.trace("updating film.description");
            builder.description(data.getDescription());
        }

        if (data.getReleaseDate() != null) {
            log.trace("updating film.releaseDate");
            builder.releaseDate(data.getReleaseDate());
        }

        if (data.getDurationInMilliseconds() != null) {
            log.trace("updating film.duration");
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

