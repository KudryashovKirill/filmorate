package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * Интерфейс, имеющий те же методы, что и FilmStorage, но на уровне сервиса.
 * @author Kirill Kydruashov
 * @version 17.0
 */
public interface FilmService {
    Film create(Film film);
    Film update(Film film);
    List<Film> getAllFilms();
    void likeFilm(Integer filmId, Integer userId);
    void deleteLike(Integer filmId, Integer userId);
    List<Film> getFilms(Integer count);
}
