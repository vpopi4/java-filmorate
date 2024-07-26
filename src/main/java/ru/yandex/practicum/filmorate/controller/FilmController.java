package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.FilmPatchDTO;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final InMemoryFilmStorage films;
    private int seq = 0;

    @GetMapping
    public List<Film> getAll() {
        return films.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody FilmDTO data) {
        log.info("handling POST /films");
        log.debug("with body={}", data);

        Integer id = getNextId();
        Film film = data.toFilm(id);

        log.info("creating film");
        log.debug("film={}", film);
        return films.create(film.getId(), film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody FilmDTO.WithId data) {
        log.info("handling PUT /films");
        log.debug("with body={}", data);

        Integer id = data.getId();
        Film film = data.toFilm(id);

        log.info("updating film");
        log.debug("film={}", film);
        return films.update(id, film);
    }

    @PatchMapping("/{id}")
    public Film updatePartially(@PathVariable Integer id,
                                @Valid @RequestBody FilmPatchDTO data) {
        log.info("handling PATCH /films/{}", id);
        log.debug("with body={}", data);

        log.debug("getting saved film");
        Optional<Film> savedFilm = films.getById(id);

        if (savedFilm.isEmpty()) {
            throw new NotFoundException("Film with id=" + id + " not found");
        }

        Film.FilmBuilder builder = savedFilm.get().toBuilder();

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

        if (data.getDuration() != null) {
            log.debug("updating film.duration");
            builder.duration(Duration.ofMinutes(data.getDuration()));
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

