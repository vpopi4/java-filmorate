package ru.yandex.practicum.filmorate.util;

public class IdGenerator {
    private int seq;

    public IdGenerator() {
        seq = 0;
    }

    public IdGenerator(int initValue) {
        seq = initValue;
    }

    public Integer getNextId() {
        return ++seq;
    }
}
