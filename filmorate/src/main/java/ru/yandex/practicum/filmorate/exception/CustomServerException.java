package ru.yandex.practicum.filmorate.exception;

public class CustomServerException extends RuntimeException {
    public CustomServerException(String message) {
        super(message);
    }
}
