package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserDAO extends EntityDAO<User> {
    boolean checkUnique(String email, String login);
}
