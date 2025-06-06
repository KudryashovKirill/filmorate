package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.CustomServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.util.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Класс, реализующий методы интерфейса FilmStorage.
 * Содержит idGenerator для автоматической генерации id фильмов, мапу Integer, Film, хранящую фильм и его id,
 * зависимость UserStorageImp для получения данных о пользователях.
 * @author Kirill Kydruashov
 * @version 17.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@Component
public class FilmStorageImp implements FilmStorage {
    static int idGenerator = 1;
    final UserStorageImp usImp;
    final Map<Integer, Film> allFilms = new HashMap<>();

    /**
     * Конструктор, автоматически внедряющий зависимость с UserStorageImp.
     * @param usImp объект UserStorageImp
     */
    @Autowired
    public FilmStorageImp(UserStorageImp usImp) {
        this.usImp = usImp;
    }

    /**
     * Метод, валидирующий фильм по его параметрам, помещающий корректный объект фильма в мапу хранения
     * и присваивающий ему id.
     * @param film объект фильма, который нужно добавить в мапу и присвоить id.
     * @throws CustomServerException Ошибка параметров объекта.
     * @throws IllegalArgumentException Выпадает, если film == null.
     * @return Film Созданный фильм.
     */
    @Override
    // учитывать аннотацию
    public Film create(Film film) {
        FilmValidator.validationAllFields(film);
        film.setId(idGenerator);
        allFilms.put(idGenerator++, film);
        log.info("Присвоение фильму id {} добавление в мапу", idGenerator);
        return film;
    }

    /**
     * Метод, предназначенный для патчинга фильма внутри мапы.
     * @param film объект фильма, предназначенный для патчинга с фильмом из мапы с таким же id
     * @throws IllegalArgumentException выпадает при условии, что film == null
     * @throws IdNotFoundException выпадает, если фильма с id, переданного в параметры метода нет в мапе
     * @return Film Обновленный фильм.
     */
    @Override
    public Film update(Film film) {
        if (film == null) {
            log.error("Исключение из-за film == null в update");
            throw new IllegalArgumentException("film must be not null");
        } else if (!allFilms.containsKey(film.getId())) {
            log.error("Исключение из-за !allFilms.containsKey(film.getId()) в update");
            throw new IdNotFoundException("no films with that id");
        }
        Film currentFilm = allFilms.get(film.getId());
        if (film.getName() != null) {
            currentFilm.setName(film.getName());
        }
        if (film.getDescription() != null) {
            currentFilm.setDescription(film.getDescription());
        }
        if (film.getReleaseDate() != null) {
            currentFilm.setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() != null) {
            currentFilm.setDuration(film.getDuration());
        }
        log.info("Патчинг фильма");
        return currentFilm;
    }

    /**
     * Метод, возвращающий значения из маппы в виде листа.
     * @return {@code List<Film>} лист со всеми фильмами из маппы.
     */
    @Override
    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(allFilms.values());
    }

    /**
     * Метод, добавляющий в пользователей, которым понравился фильм, пользователя с userId.
     * @param filmId Id фильма, который нужно лайкнуть.
     * @param userId Id пользователя, ставящего лайк.
     * @throws IdNotFoundException Выпадает, если пользователя нет в мапе хранения.
     * @throws IdNotFoundException Выпадает, если фильма нет в мапе хранения.
     */
    @Override
    public void likeFilm(Integer filmId, Integer userId) {
        validateContainsInAllUsers(userId);
        validateContainsInAllFilms(filmId);
        Film film = allFilms.get(filmId);
        if (film.getUserWhoLikeFilm() == null) {
            film.setUserWhoLikeFilm(new HashSet<>());
        }
        film.getUserWhoLikeFilm().add(userId);
        log.info("Постановка лайка фильму {} от юзера {}", filmId, userId);
    }

    /**
     * Метод, удаляющий пользователя с userId из сета пользователей, которым понравился фильм.
     * @param filmId Id фильма, у которого нужно удалить лайк.
     * @param userId Id пользователя, удаляющего лайк.
     * @throws IdNotFoundException Выпадает, если пользователя нет в мапе хранения.
     * @throws IdNotFoundException Выпадает, если фильма нет в мапе хранения.
     */
    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        validateContainsInAllFilms(filmId);
        validateContainsInAllUsers(userId);
        boolean removed = allFilms.get(filmId).getUserWhoLikeFilm().remove(userId);

        if (!removed) {
            log.warn("Лайк от пользователя {} к фильму {} не найден", userId, filmId);
            throw new IdNotFoundException("like not found");
        }
        allFilms.get(filmId).getUserWhoLikeFilm().remove(userId);
        log.info("Удаление лайка фильму {} от юзера {}", filmId, userId);
    }

    /**
     * Метод, сортирующий фильмы по количеству лайков.
     * @param count Количество фильмов, переданных для сортировки, дефолтное значение 10.
     * @return {@code List<Film>} Лист фильмов, отсортированных по лайкам.
     */
    @Override
    public List<Film> getFilms(Integer count) {
        log.info("Получение всех фильмов по лайкам до {}", count);
        return allFilms.values().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getUserWhoLikeFilm().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilm(Integer id) {
        validateContainsInAllFilms(id);
        return allFilms.get(id);
    }

    private void validateContainsInAllUsers(Integer userId) {
        if (!usImp.getAllUsers().containsKey(userId)) {
            log.error("Исключение из-за !usImp.getAllUsers().containsKey(userId) в deleteFilm");
            throw new IdNotFoundException("no user with that id");
        }
    }

    private void validateContainsInAllFilms(Integer filmId) {
        if (!allFilms.containsKey(filmId)) {
            log.error("Исключение из-за !allFilms.containsKey(filmId) в likeFilm");
            throw new IdNotFoundException("no film with that id");
        }
    }
}
