package ru.yandex.practicum.filmorate.exception;

public class NotCorrectParamsException extends RuntimeException {
    public NotCorrectParamsException(String message) {
        super(message);
    }
}
