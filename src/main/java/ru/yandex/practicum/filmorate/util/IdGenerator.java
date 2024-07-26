package ru.yandex.practicum.filmorate.util;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
    private int seq;

    public IdGenerator() {
        seq = 0;
    }

    public Integer getNextId() {
        return ++seq;
    }
}
