package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface EntityDAO<E> {
    Optional<E> getById(Integer id);

    List<E> getAll();

    E create(E entity) throws AlreadyExistException, DataAccessException;

    E update(E entity) throws NotFoundException, DataAccessException;

    void delete(Integer id) throws DataAccessException;
}
