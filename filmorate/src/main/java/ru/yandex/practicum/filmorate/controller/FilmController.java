package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotCorrectParamsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.CustomServerException;
import ru.yandex.practicum.filmorate.util.ErrorResponse;

import java.util.List;

/**
 * Контроллер для фильмов
 */
@Slf4j
@RestController
@RequestMapping("/films") // обычно начинается сверху вниз
public class FilmController {
    private FilmService filmService;
    /**
     * Создает экземпляр контроллера и внедряет зависимость FilmService.
     * @param interfaceFilm Объект класса, имплементирующего FilmService.
     */
    @Autowired
    public FilmController(FilmService interfaceFilm) {
        this.filmService = interfaceFilm;
    }

    @Operation(summary = "Создание фильма", description = "Создание фильма в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фильм создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях фильма")
    })
    /**
     * Post запрос для создания фильма.
     * @param film Фильм, который необходимо создать.
     * @return {@code ResponseEntity<Film>} Созданный фильм в JSON виде.
     */
    @PostMapping
    public ResponseEntity<Film> createFilm(@Parameter (description = "Фильм предназначенный для создания", required = true)
                                               @RequestBody Film film) {
        log.info("Получен POST запрос на создание фильма");
        Film createdFilm = filmService.create(film);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }
    @Operation(summary = "Обновление фильма", description = "Обновление фильма в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фильм обновлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях фильма"),
            @ApiResponse(responseCode = "404", description = "Отсутствие фильма в мапе хранения")
    })
    /**
     * Put запрос для обновления фильма.
     * @param film Фильм, который необходимо обновить.
     * @return {@code ResponseEntity<Film>} Обновленный фильм в JSON виде.
     */
    @PutMapping
    public ResponseEntity<Film> updateFilm(@Parameter(description = "Фильм предназначенный для обновления", required = true)
                                               @RequestBody Film film) {
        log.info("Получен POST запрос на обновление фильма");
        Film updatedFilm = filmService.update(film);
        return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
    }

    @Operation(summary = "Получение всех фильмов", description = "Получение всех фильмов из мапы в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Получение всех фильмов"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях фильма")
    })
    /**
     * Get запрос для получения всех фильмов.
     * @return {@code ResponseEntity<List<Film>>} Лист JSON объектов всех фильмов.
     */
    @GetMapping
    public ResponseEntity<List<Film>> getAllIFilms() {
        log.info("Получен GET запрос на получение всех фильмов");
        List<Film> allFilms = filmService.getAllFilms();
        return new ResponseEntity<>(allFilms, HttpStatus.OK);
    }

    @Operation(summary = "Постановка лайка фильму", description = "Постановка лайка фильму в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Лайк поставлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях фильма"),
            @ApiResponse(responseCode = "404", description = "Отсутствие фильма в мапе хранения")
    })
    /**
     * Put запрос для того, чтобы лайкнуть фильм.
     * @param filmId Id фильма из ссылки запроса, которому нужно поставить лайк.
     * @param userId Id пользователя из ссылки запроса, который ставит лайк.
     */
    @PutMapping("{filmId}/like/{userId}")
    public ResponseEntity<Void> likeFilm(@Parameter(description = "Id фильма, который нужно лайкнуть", required = true)
                                             @PathVariable Integer filmId, @Parameter
            (description = "Id пользователя, ставящего лайк", required = true) @PathVariable Integer userId) {
        log.info("Получен PUT запрос на постановку лайка фильму {} от юзера {}", filmId, userId);
        filmService.likeFilm(filmId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "удаление лайка у фильма", description = "Удаление лайка у фильма в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Лайк удален"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях фильма"),
            @ApiResponse(responseCode = "404", description = "Отсутствие фильма в мапе хранения")
    })
    /**
     * Delete запрос для того, чтобы удалить лайк у фильма.
     * @param filmId Id фильма из ссылки запроса, у которого нужно удалить лайк.
     * @param userId Id пользователя из ссылки запроса, который удаляет лайк.
     */
    @DeleteMapping("{filmId}/like/{userId}")
    public ResponseEntity<Void> deleteLike(@Parameter(description = "Id фильма, у которого нужно удалить лайк",
            required = true)  @PathVariable Integer filmId, @Parameter (description = "Id пользователя, удаляющего лайк",
            required = true) @PathVariable Integer userId) {
        log.info("Получен DELETE запрос на удаление лайка фильму {} от юзера {}", filmId, userId);
        filmService.deleteLike(filmId, userId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Получение всех фильмов по лайкам", description = "Получение всех фильмов по лайкам в " +
            "контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фильмы получены по количеству лайков"),
    })
    /**
     * Get запрос для сортировки фильмов по количеству лайков.
     * @param count Количество фильмов для сортировки.
     * @return {@code ResponseEntity<List<Film>>} Лист отсортированных по лайкам фильмов.
     */
    @GetMapping("popular")
    public ResponseEntity<List<Film>> getFilms(@Parameter(description = "Количество фильмов для получения", required = true)
                                                   @RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен GET запрос на получение всех фильмов по колл-ву лайков до {}", count);
        List<Film> films = filmService.getFilms(count);
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @ExceptionHandler(CustomServerException.class)
    public ResponseEntity<?> handleCustomException(CustomServerException ex) {
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST) // todo уточнить как использовать
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getClass().getName(),
                ex.getMessage()));

    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<?> handleCustomException(IdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getClass().getName(),
                ex.getMessage()));
    }
    @ExceptionHandler(NotCorrectParamsException.class)
    public ResponseEntity<?> handleCustomException(NotCorrectParamsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getClass().getName(),
                ex.getMessage()));
    }
}
