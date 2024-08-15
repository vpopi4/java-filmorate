package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.FilmPatchDTO;
import ru.yandex.practicum.filmorate.dto.NewFilmDTO;
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
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody NewFilmDTO.Request.Create data) {
        Film film = service.create(data);
        log.debug("film was created: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody FilmDTO.WithId data) {
        Film film = service.update(data);
        log.debug("film was updated: {}", film);
        return film;
    }

    @PatchMapping("/{id}")
    public Film updatePartially(@PathVariable Integer id,
                                @Valid @RequestBody FilmPatchDTO data) {
        Film film = service.updatePartially(id, data);
        log.debug("film was updated: {}", film);
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film putLike(@PathVariable Integer filmId,
                        @PathVariable Integer userId) {
        return service.putLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable Integer filmId,
                           @PathVariable Integer userId) {
        return service.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return service.getPopularFilms(count);
    }
}
