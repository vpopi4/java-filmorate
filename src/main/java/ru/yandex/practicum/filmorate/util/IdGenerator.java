package ru.yandex.practicum.filmorate.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IdGenerator {
    private int seq;

    public IdGenerator(int initValue) {
        seq = initValue;
    }

    public Integer getNextId() {
        return ++seq;
    }
}
