package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.FilmPatchDTO;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmDao;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final IdGenerator idGenerator;
    private final UserService userService;
    private final FilmDao storage;

    public List<Film> getAll() {
        return storage.getAll();
    }

    public Film getById(Integer id) throws NotFoundException {
        return storage
                .getById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Film[id=" + id + "] not found"
                ));
    }

    public Film create(FilmDTO dto) throws AlreadyExistException {
        Integer id = idGenerator.getNextId();
        Film film = Film.builder()
                .id(id)
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(Duration.ofMinutes(dto.getDuration()))
                .build();

        return storage.create(film);
    }

    public Film update(FilmDTO.WithId dto) throws NotFoundException {
        Film film = getById(dto.getId()).toBuilder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(Duration.ofMinutes(dto.getDuration()))
                .build();

        return storage.update(film);
    }

    public Film updatePartially(Integer id, FilmPatchDTO dto) throws NotFoundException {
        Film.FilmBuilder builder = getById(id).toBuilder();

        if (dto.getName() != null) {
            log.debug("updating film.name");
            builder.name(dto.getName());
        }

        if (dto.getDescription() != null) {
            log.debug("updating film.description");
            builder.description(dto.getDescription());
        }

        if (dto.getReleaseDate() != null) {
            log.debug("updating film.releaseDate");
            builder.releaseDate(dto.getReleaseDate());
        }

        if (dto.getDuration() != null) {
            log.debug("updating film.duration");
            builder.duration(Duration.ofMinutes(dto.getDuration()));
        }

        Film film = builder.build();

        return storage.update(film);
    }

    public void delete(Integer id) {
        storage.delete(id);
    }

    public Film putLike(Integer filmId, Integer userId) throws NotFoundException {
        Film film = getById(filmId);
        User user = userService.getById(userId);

        film.getLikesUserId().add(user.getId());

        return storage.update(film);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = getById(filmId);
        User user = userService.getById(userId);

        film.getLikesUserId().remove(user.getId());

        return storage.update(film);
    }

    public List<Film> getPopularFilms(Integer count) {
        return getAll()
                .stream()
                .sorted((f1, f2) -> Integer.compare(
                        f2.getLikesUserId().size(),
                        f1.getLikesUserId().size()
                ))
                .limit(count)
                .toList();
    }
}
