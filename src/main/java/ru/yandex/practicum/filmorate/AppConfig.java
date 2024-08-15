package ru.yandex.practicum.filmorate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.storage.interfaces.Dao;
import ru.yandex.practicum.filmorate.util.IdGenerator;

@Configuration
public class AppConfig {
    @Bean
    public IdGenerator idGenerator(Dao<?> storage) {
        return new IdGenerator(storage.getMaxId());
    }
}
