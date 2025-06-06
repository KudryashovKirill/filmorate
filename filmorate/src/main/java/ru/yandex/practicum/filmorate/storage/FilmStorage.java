package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * Интерфейс с методами всей бизнес логики для сущности фильм приложения Filmorate
 * (спросить про это)
 * Данные методы позволяют создать,
 * обновить фильм, получить все фильмы, лайкнуть фильм и удалить лайк к нему, получить заданное число фильмов,
 * отсортированных по количеству лайков.
 * @author Kirill Kydryashov.
 * @version 17.0
 */
public interface FilmStorage {
    Film create(Film film);
    Film update(Film film);
    List<Film> getAllFilms();
    void likeFilm(Integer filmId, Integer userId);
    void deleteLike(Integer filmId, Integer userId);
    List<Film> getFilms(Integer count);
    Film getFilm(Integer id);
}
