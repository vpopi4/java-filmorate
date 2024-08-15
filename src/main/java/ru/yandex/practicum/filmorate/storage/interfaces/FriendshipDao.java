package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FriendshipDao {
    Friendship create(Friendship entity) throws AlreadyExistException, DataAccessException;

    Friendship update(Friendship entity) throws NotFoundException, DataAccessException;

    Set<User> getUserFriends(Integer userId) throws DataAccessException;

    Optional<Friendship> getByIds(Integer userId, Integer friendId) throws DataAccessException;

    void delete(Integer userId, Integer friendId) throws DataAccessException;
}
