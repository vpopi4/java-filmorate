package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getAll() throws DataAccessException;

    Optional<User> getById(Integer id) throws DataAccessException;

    User create(User user) throws AlreadyExistException, DataAccessException;

    User update(User entity) throws NotFoundException, AlreadyExistException, DataAccessException;

    void delete(Integer id) throws DataAccessException;
}
