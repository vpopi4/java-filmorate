package ru.yandex.practicum.filmorate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.Dao;
import ru.yandex.practicum.filmorate.util.IdGenerator;

@Configuration
public class AppConfig {
    @Bean
    public IdGenerator userIdGenerator(Dao<User> storage) {
        return new IdGenerator(storage.getMaxId());
    }

    @Bean
    public IdGenerator filmIdGenerator(Dao<Film> storage) {
        return new IdGenerator(storage.getMaxId());
    }
}
