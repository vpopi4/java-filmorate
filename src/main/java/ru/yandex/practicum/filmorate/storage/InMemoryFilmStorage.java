package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

@Repository
public class InMemoryFilmStorage extends InMemoryEntityStorage<Film> {

}

