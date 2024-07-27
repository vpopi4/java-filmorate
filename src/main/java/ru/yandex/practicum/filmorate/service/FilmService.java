package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.FilmPatchDTO;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.EntityStorage;
import ru.yandex.practicum.filmorate.util.FilmMapper;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final EntityStorage<Film> storage;
    private final IdGenerator idGenerator;

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
        HashSet<Integer> likes = new HashSet<>();
        Film film = FilmMapper.map(dto, id, likes);

        return storage.create(id, film);
    }

    public Film update(FilmDTO.WithId dto) throws NotFoundException {
        HashSet<Integer> likes = new HashSet<>();
        Film film = FilmMapper.map(dto, likes);

        return storage.update(film.getId(), film);
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

        return storage.put(id, film);
    }

    public void delete(Integer id) {
        // TODO: check data safety (users)
        storage.remove(id);
    }
}
