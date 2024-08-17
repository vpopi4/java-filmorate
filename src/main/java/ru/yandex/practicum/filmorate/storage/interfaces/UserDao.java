package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

public interface UserDao extends Dao<User> {
    Optional<User> getByEmail(String email);

    Optional<User> getByLogin(String login);
}
