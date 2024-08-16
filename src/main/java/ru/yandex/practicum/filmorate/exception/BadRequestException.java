package ru.yandex.practicum.filmorate.exception;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException() {
    }

    public BadRequestException(String s) {
        super(s);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
