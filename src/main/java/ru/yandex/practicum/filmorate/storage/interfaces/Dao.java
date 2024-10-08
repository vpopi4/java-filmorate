package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;

public interface Dao<E> {
    Optional<E> getById(Integer id) throws DataAccessException;

    List<E> getAll() throws DataAccessException;

    void create(E entity) throws DataAccessException;

    void update(E entity) throws DataAccessException;

    void delete(Integer id) throws DataAccessException;

    Integer getMaxId() throws DataAccessException;
}
