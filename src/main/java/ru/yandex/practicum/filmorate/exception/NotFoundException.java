package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

import java.util.NoSuchElementException;

public class NotFoundException extends NoSuchElementException {
    public NotFoundException() {
    }

    public NotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String s) {
        super(s);
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
