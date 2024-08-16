package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDTO;
import ru.yandex.practicum.filmorate.dto.GenreDTO;
import ru.yandex.practicum.filmorate.dto.MpaRatingDTO;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmDtoMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.JdbcFilmDao;
import ru.yandex.practicum.filmorate.storage.JdbcGenreDao;
import ru.yandex.practicum.filmorate.storage.interfaces.Dao;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final IdGenerator filmIdGenerator;
    private final UserService userService;
    private final JdbcFilmDao storage;
    private final Dao<MpaRating> mpaRatingStorage;
    private final JdbcGenreDao genreStorage;

    public List<Film> getAll() {
        // TODO: optimize getting genres and likes
        return storage.getAll().stream()
                .map(film -> film.toBuilder()
                        .genres(storage.getGenres(film.getId()))
                        .likesUserId(storage.getLikesUserId(film.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public Film getById(Integer id) throws NotFoundException {
        return storage
                .getById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Film[id=" + id + "] not found"
                ))
                .toBuilder()
                .genres(storage.getGenres(id))
                .likesUserId(storage.getLikesUserId(id))
                .build();
    }

    public Film create(FilmDTO.Request.Create dto) throws AlreadyExistException {
        Integer id = filmIdGenerator.getNextId();

        List<Genre> genres = findGenres(dto.getGenres());

        Film film = FilmDtoMapper.map(
                dto,
                id,
                findMpaRating(dto.getMpa()),
                genres
        );

        storage.create(film);
        storage.setGenres(film.getId(), film.getGenres());

        return getById(id);
    }

    public Film update(FilmDTO.Request.Update dto) throws NotFoundException {
        Film film = FilmDtoMapper.map(
                dto,
                findMpaRating(dto.getMpa()),
                findGenres(dto.getGenres())
        );

        storage.update(film);
        storage.setGenres(film.getId(), film.getGenres());

        return getById(dto.getId());
    }

    private MpaRating findMpaRating(MpaRatingDTO mpa) {
        return mpaRatingStorage.getById(mpa.id())
                .orElseThrow(() -> new BadRequestException("mpa rating not found"));
    }

    private List<Genre> findGenres(List<GenreDTO> genresIds) {
        if (genresIds == null) {
            return Collections.emptyList();
        }

        Set<GenreDTO> uniqueGenres = new HashSet<>(genresIds);

        return uniqueGenres.stream()
                .map(g -> genreStorage
                        .getById(g.id())
                        .orElseThrow(() -> new BadRequestException("Such genre[id" + g.id() + "] does not exist"))
                )
                .collect(Collectors.toList());
    }

    public Film updatePartially(Integer id, FilmDTO.Request.UpdatePartially dto) throws NotFoundException {
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

        storage.update(film);

        return getById(id);
    }

    public void delete(Integer id) {
        storage.delete(id);
    }

    public Film putLike(Integer filmId, Integer userId) throws NotFoundException {
        storage.getById(filmId).orElseThrow(NotFoundException::new);
        userService.getById(userId);

        storage.putLike(filmId, userId);

        return getById(filmId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        storage.getById(filmId).orElseThrow(NotFoundException::new);
        userService.getById(userId);

        storage.deleteLike(filmId, userId);

        return getById(filmId);
    }

    public List<Film> getPopularFilms(Integer count) {
        // TODO: optimize by sql query
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
