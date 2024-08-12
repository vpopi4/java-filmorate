package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    List<Film> getAll() throws DataAccessException;

    Optional<Film> getById(Integer id) throws DataAccessException;

    Film create(Film film) throws AlreadyExistException, DataAccessException;

    Film update(Film film) throws NotFoundException, DataAccessException;

    void delete(Integer id) throws DataAccessException;
}
