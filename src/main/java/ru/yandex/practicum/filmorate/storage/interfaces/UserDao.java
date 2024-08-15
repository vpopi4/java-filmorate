package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.NewEntityDao;

import java.util.Optional;

public interface UserDao extends NewEntityDao<User> {
    Optional<User> getByEmail(String email);

    Optional<User> getByLogin(String login);
}
