package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.CustomServerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
@UtilityClass
@Slf4j
/**
 * Класс для валидации фильмов 
 */
public final class FilmValidator {
    /**
     * Метод, валидирующий фильм по всем его полям.
     * @param film Фильм для валидации
     */
    public void validationAllFields(Film film) {
        if (film == null) {
            log.error("Исключение из-за film == null");
            throw new IllegalArgumentException("film must be not null");
        } else if (film.getName().isEmpty()) {
            log.error("Исключение из-за film.getName().isEmpty()");
            throw new CustomServerException("name must be not null");
        } else if (film.getDescription().length() > 200) {
            log.error("Исключение из-за film.getDescription().length() > 200");
            throw new CustomServerException("description must be less than 200 symbols");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Исключение из-за film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)");
            throw new CustomServerException("film must be released after 28-12-1895");
        } else if (film.getDuration() < 0) {
            log.error("Исключение из-за film.getDuration() < 0");
            throw new CustomServerException("duration must be positive");
        }
    }

    public static void validationName(Film film) {
        if (film.getName().isEmpty()) {
            throw new CustomServerException("name must be not null");
        }
    }

    public static void validationDescription(Film film) {
        if (film.getDescription().length() > 200) {
            throw new CustomServerException("description must be less than 200 symbols");
        }
    }

    public static void validationReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new CustomServerException("film must be released after 28-12-1895");
        }
    }

    public static void validationDuration(Film film) {
        if (film.getDuration() < 1) {
            throw new CustomServerException("duration must be positive");
        }
    }
}
