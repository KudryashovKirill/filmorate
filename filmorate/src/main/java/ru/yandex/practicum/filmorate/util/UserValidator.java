package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.CustomServerException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void validationAllFields(User user) {
        if (user == null) {
            log.error("Исключение из-за user == null");
            throw new IllegalArgumentException("user must be not null");
        } else if (user.getEmail() != null) {
            if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
                log.error("Исключение из-за user.getEmail().isEmpty() || !user.getEmail().contains(\"@\")");
                throw new CustomServerException("email must be right");
            }
        } else if (user.getLogin() != null) {
            if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                log.error("Исключение из-за user.getLogin().isEmpty() || user.getLogin().contains(\" \")");
                throw new CustomServerException("login must be right");
            }
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Исключение из-за user.getBirthday().isAfter(LocalDate.now())");
                throw new CustomServerException("birthday must be not in future");
            }
        }
    }

    public static void validationEmail(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new CustomServerException("email must be right");
        }
    }

    public static void validationLogin(User user) {
        if (user.getLogin() != null) {
            if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                throw new CustomServerException("login must be right");
            }
        }
    }

    public static void validationName(User user) {
        if (user.getName() != null) {
            throw new CustomServerException("name must be not null");
        }
    }

    public static void validationBirthday(User user) {
        if (user.getBirthday() != null) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new CustomServerException("birthday must be not in future");
            }
        }
    }
}
