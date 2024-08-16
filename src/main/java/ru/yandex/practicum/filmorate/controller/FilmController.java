package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> getAll() {
        log.debug("handle GET /films");

        return service.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        log.debug("handle GET /films/{id}");
        log.debug("id={}", id);

        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody FilmDTO.Request.Create dto) {
        log.debug("handle POST /films");
        log.debug("request body={}", dto);

        Film film = service.create(dto);
        log.info("film was created: {}", film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody FilmDTO.Request.Update dto) {
        log.debug("handle PUT /films");
        log.debug("request body={}", dto);

        Film film = service.update(dto);
        log.info("film was updated: {}", film);

        return film;
    }

    @PatchMapping("/{id}")
    public Film updatePartially(@PathVariable Integer id,
                                @Valid @RequestBody FilmDTO.Request.UpdatePartially dto) {
        log.debug("handle PATCH /films/{id}");
        log.debug("id={}, request body={}", id, dto);

        Film film = service.updatePartially(id, dto);
        log.info("film was updated: {}", film);

        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film putLike(@PathVariable Integer filmId,
                        @PathVariable Integer userId) {
        log.debug("handle PUT /films/{filmId}/like/{userId}");
        log.debug("filmId={}, userId={}", filmId, userId);

        Film film = service.putLike(filmId, userId);
        log.info("like was added to film={}", film);

        return film;
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable Integer filmId,
                           @PathVariable Integer userId) {
        log.debug("handle DELETE /films/{filmId}/like/{userId}");
        log.debug("filmId={}, userId={}", filmId, userId);

        Film film = service.deleteLike(filmId, userId);
        log.info("like was removed from film={}", film);

        return film;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.debug("handle GET /films/popular?count={}", count);

        return service.getPopularFilms(count);
    }
}
