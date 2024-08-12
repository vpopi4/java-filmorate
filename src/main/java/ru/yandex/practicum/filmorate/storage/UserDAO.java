package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAll() throws DataAccessException;

    User getById(Integer id) throws NotFoundException, DataAccessException;

    User getByEmail(String email) throws NotFoundException, DataAccessException;

    User getByLogin(String login) throws NotFoundException, DataAccessException;

    User create(User user) throws AlreadyExistException, DataAccessException;

    User update(User entity) throws NotFoundException, AlreadyExistException, DataAccessException;

    void delete(Integer id) throws DataAccessException;
}
