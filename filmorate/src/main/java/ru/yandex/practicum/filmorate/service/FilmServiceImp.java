package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

/**
 * Сервис для работы с фильмами, использующий FilmStorage для выполнения операций.
 * Реализует интерфейс FilmService.
 *
 * @author Kirill Kydruashov
 * @version 17.0
 */
@Slf4j
@Service
public class FilmServiceImp implements FilmService {
    private FilmStorage filmStorage;

    /**
     * Создает экземпляр FilmServiceImp с внедренной зависимостью FilmStorage.
     * @param filmStorage Объект класса, имплементирующего FilmStorage.
     */
    @Autowired
    public FilmServiceImp(@Qualifier("filmStorageDbImp") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    /**
     * Метод, в котором дублируется реализация метода создания фильма.
     *
     * @param film Экземпляр фильма, который нужно создать.
     * @return Film Созданный фильм.
     */
    @Override
    public Film create(Film film) {
        log.info("Создание в сервисе фильма {}", film.getId());
        return filmStorage.create(film);
    }

    /**
     * Метод, в котором дублируется реализация метода обновления фильма.
     *
     * @param film Экземпляр фильма, который нужно обновить.
     * @return Film Обновленный фильм.
     */
    @Override
    public Film update(Film film) {
        log.info("Обновление в сервисе фильма {}", film.getId());
        return filmStorage.update(film);
    }

    /**
     * Метод, в котором дублируется реализация метода получения всех фильмов.
     *
     * @return {@code List<Film>} Лист со всеми фильмами.
     */
    @Override
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    /**
     * Метод, в котором дублируется реализация метода лайка фильма.
     *
     * @param filmId Id фильма, который нужно лайкнуть.
     * @param userId Id пользователя, который ставит лайк.
     */
    @Override
    public void likeFilm(Integer filmId, Integer userId) {
        log.info("Постановка лайка в сервисе на фильм {} от юзера {}", filmId, userId);
        filmStorage.likeFilm(filmId, userId);
    }

    /**
     * Метод, в котором дублируется реализация удаления лайка у фильма.
     *
     * @param filmId Id фильма, у которого нужно удалить лайк.
     * @param userId Id пользователя, который удаляет лайк.
     */
    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        log.info("Удаление лайка в сервисе на фильм {} от юзера {}", filmId, userId);
        filmStorage.deleteLike(filmId, userId);
    }

    /**
     * Метод, в котором дублируется реализация метода получения фильмов по количеству лайков.
     * @param count Количество фильмов, необходимых для сортировки.
     * @return {@code List<Film>} Лист отсортированных фильмов.
     */
    @Override
    public List<Film> getFilms(Integer count) {
        log.info("Получение всех фильмов в сервисе");
        return filmStorage.getFilms(count);
    }
}
