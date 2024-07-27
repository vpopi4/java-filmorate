package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.util.*;

public class InMemoryEntityStorage<E> implements EntityStorage<E> {
    protected final Map<Integer, E> storage;

    protected InMemoryEntityStorage() {
        storage = new HashMap<>();
    }

    @Override
    public Optional<E> getById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<E> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public E create(Integer id, E entity) throws AlreadyExistException {
        if (storage.containsKey(id)) {
            throw new AlreadyExistException();
        }

        return put(id, entity);
    }

    @Override
    public E update(Integer id, E entity) throws NotFoundException {
        if (!storage.containsKey(id)) {
            throw new NotFoundException();
        }

        return put(id, entity);
    }

    @Override
    public E put(Integer id, E entity) {
        storage.put(id, entity);
        return entity;
    }

    @Override
    public void remove(Integer id) {
        storage.remove(id);
    }
}
