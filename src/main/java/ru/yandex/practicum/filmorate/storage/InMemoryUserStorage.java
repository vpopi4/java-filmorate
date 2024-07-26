package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

@Repository
public class InMemoryUserStorage extends InMemoryEntityStorage<User> {

}
