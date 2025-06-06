package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.CustomServerException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotCorrectParamsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class FilmStorageDbImp implements FilmStorage {

    JdbcTemplate jdbcTemplate;
    SimpleJdbcInsert insert;

    @Autowired
    public FilmStorageDbImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Film create(Film film) {
        if (film == null) {
            log.error("Невозможно создать фильм тк он null");
            throw new CustomServerException("");
        }
        if (film.getName().isBlank() || film.getDescription().length() > 200 ||
            film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) || film.getDuration() < 0) {
            log.error("неверные данные фильма с id {}", film.getId());
            throw new NotCorrectParamsException("");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration", film.getDuration());

        Number filmId = insert.executeAndReturnKey(params);
        film.setId(filmId.intValue());
        log.info("Создан фильм {} и добавлен в таблицу", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film == null) {
            log.error("Не распарсился JSON");
            throw new IllegalArgumentException();
        }

        boolean filmExists = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM films WHERE id = ?",
                Integer.class,
                film.getId()
        ) > 0;
        if (!filmExists) {
            throw new IdNotFoundException("");
        }
        String sqlQuery = """
                UPDATE films
                SET name = ?, description = ?, release_date = ?, duration = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        log.info("Фильм запатчен {} название: {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new Film(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration")
                ));
        log.info("Полученные все фильмы");
        return films;
    }

    @Override
    public Film getFilm(Integer id) {
        String sqlQuery = "SELECT * FROM films WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, Film.class, id);
        log.info("получен фильм {}", film);
        return film;
    }

    @Override
    public void likeFilm(Integer filmId, Integer userId) {
        if (!userExists(userId)) {
            log.error("нет такого пользователя для лайка id {}", filmId);
            throw new IdNotFoundException("");
        }
        if (!filmExistById(filmId)) {
            log.error("нет такого фильма для лайка id {}", filmId);
            throw new IdNotFoundException("");
        }
        String sqlQuery = """
                INSERT INTO likes (film_id, user_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Добавлен лайк от юзера {} на фильм {}", userId, filmId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        if (!userExists(userId)) {
            log.error("нет такого пользователя для удаления лайка id {}", filmId);
            throw new IdNotFoundException("");
        }
        if (!filmExistById(filmId)) {
            log.error("нет такого фильма для удаления лайка id {}", filmId);
            throw new IdNotFoundException("");
        }
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("удален лайк от юзера {} на фильм {}", userId, filmId);
    }

    @Override
    public List<Film> getFilms(Integer count) {
        count = count == null ? 10 : count;

        String sqlQuery = """
                SELECT f.id, f.name, f.description, f.release_date, f.duration,
                       COALESCE(COUNT(DISTINCT l.user_id), 0) AS likes_count
                FROM films f
                LEFT JOIN likes l ON f.id = l.film_id
                GROUP BY f.id
                ORDER BY likes_count DESC
                LIMIT ?
                """;
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new Film(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration")
                ), count);
        log.info("получены фильмы LIMIT {}", count);
        return films;
    }

    private boolean filmExistByFilm(Film film) {
        String sqlForFilmExists = "SELECT COUNT(*) FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(
                sqlForFilmExists,
                Integer.class,
                film.getId()
        ) > 0;
    }

    private boolean userExists(Integer id) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count > 0;
    }

    private boolean filmExistById(Integer id) {
        String sqlQuery = "SELECT COUNT(*) FROM films WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count > 0;
    }
}
