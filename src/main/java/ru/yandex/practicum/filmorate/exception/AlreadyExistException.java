package ru.yandex.practicum.filmorate.exception;

public class AlreadyExistException extends IllegalArgumentException {
    public AlreadyExistException() {
    }

    public AlreadyExistException(String s) {
        super(s);
    }

    public AlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistException(Throwable cause) {
        super(cause);
    }
}
