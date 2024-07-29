package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface EntityStorage<E> {
    Optional<E> getById(Integer id);

    List<E> getAll();

    E create(Integer id, E entity) throws AlreadyExistException;

    E update(Integer id, E entity) throws NotFoundException;

    E put(Integer id, E entity);

    void remove(Integer id);
}
