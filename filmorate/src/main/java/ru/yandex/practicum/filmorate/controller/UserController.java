package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.CustomServerException;
import ru.yandex.practicum.filmorate.exception.NotCorrectParamsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.ErrorResponse;

import java.util.List;

/**
 * Контроллер для пользователей
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Создает экземпляр контроллера и внедряет зависимость UserService.
     *
     * @param interfaceUser Объект класса, имплементирующего UserService.
     */
    @Autowired
    public UserController(UserService interfaceUser) {
        this.userService = interfaceUser;
    }

    @Operation(summary = "Создание пользователя", description = "Создание пользователя в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях пользователя")
    })
    /**
     * Post запрос для создания пользователя.
     *
     * @param user Пользователь, которого необходимо создать.
     * @return {@code ResponseEntity<User>} Созданный пользователь в JSON виде.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Parameter(description = "Пользователь предназначенный для создания",
            required = true) @RequestBody User user) {
        log.info("Получен POST запрос на создание пользователя");
        User createdUser = userService.create(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновление пользователя", description = "Обновление пользователя в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях пользователя"),
            @ApiResponse(responseCode = "404", description = "Отсутствие пользователя в мапе хранения")
    })
    /**
     * Put запрос для обновления фильма.
     *
     * @param user Пользователь, который необходимо обновить.
     * @return {@code ResponseEntity<User>} Обновленный пользователь в JSON виде.
     */
    @PutMapping
    public ResponseEntity<User> updateUser(@Parameter(description = "Пользователь предназначенный для создания",
            required = true) @RequestBody User user) {
        log.info("Получен POST запрос на обновление пользователя");
        User updatedUser = userService.update(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Получение всех пользователей", description = "Получение всех пользователей из мапы в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Получение всех фильмов"),
    })
    /**
     * Get запрос для получения всех пользователей.
     * @return {@code ResponseEntity<List<User>>} Лист JSON объектов всех пользователей.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllIUsers() {
        log.info("Получен GET запрос на получение всех пользователей");
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @Operation(summary = "Добавление в друзья", description = "Добавление в друзья в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Друг добавлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях одного из пользователей"),
            @ApiResponse(responseCode = "404", description = "Отсутствие одного из пользователей в мапе хранения")
    })
    /**
     * Put запрос для добавления в друзья.
     *
     * @param userId   Id юзера из ссылки запроса.
     * @param friendId Id друга из ссылки запроса.
     */
    @PutMapping("{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@Parameter(description = "Id одного из друзей",
            required = true) @PathVariable Integer userId, @Parameter(description = "Id другого пользователя",
            required = true) @PathVariable Integer friendId) {
        log.info("Получен PUT запрос на добавление друга {} к юзеру {}", friendId, userId);
        userService.addFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление из друзей", description = "Удаление из друзей в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Друг удален"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях одного из пользователей"),
            @ApiResponse(responseCode = "404", description = "Отсутствие одного из пользователей в мапе хранения")
    })
    /**
     * Delete запрос для удаления из друзей.
     *
     * @param userId   Id юзера из ссылки запроса.
     * @param friendId Id друга из ссылки запроса.
     */
    @DeleteMapping("{userId}/friends/{friendId}")
    public ResponseEntity<Void> deleteFriend(@Parameter(description = "Id одного из друзей",
            required = true) @PathVariable Integer userId, @Parameter(description = "Id другого пользователя",
            required = true) @PathVariable Integer friendId) {
        log.info("Получен DELETE запрос на удаление друга {} у юзера {}", friendId, userId);
        userService.deleteFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение всех друзей пользователя", description = "Получение всех друзей пользователя" +
                                                                             " из мапы в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Получение всех друзей"),
    })
    /**
     * Get запрос для получения всех друзей.
     *
     * @param userId Id юзера из ссылки запроса.
     * @return {@code ResponseEntity<List<User>>} Лист всех друзей пользователя в JSON формате.
     */
    @GetMapping("{userId}/friends")
    public ResponseEntity<List<User>> getAllFriends(@Parameter(description = "Id пользователя, у которого нужно " +
                                                                             "получить всех друзей", required = true) @PathVariable Integer userId) {
        log.info("Получен GET запрос на получение всех друзей у юзера {}", userId);
        List<User> allFriendsOfUser = userService.getAllFriends(userId);
        return new ResponseEntity<>(allFriendsOfUser, HttpStatus.OK);
    }

    @Operation(summary = "Получение всех общих друзей пользователей", description = "Получение общих друзей пользователей" +
                                                                                    " из мапы в контроллере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Получение всех друзей"),
            @ApiResponse(responseCode = "400", description = "Ошибка в полях одного из пользователей"),
            @ApiResponse(responseCode = "404", description = "Отсутствие одного из пользователей в мапе хранения")
    })
    /**
     * Get запрос для получения всех общих друзей.
     *
     * @param userId   Id юзера из ссылки запроса.
     * @param friendId Id друга из ссылки запроса.
     * @return {@code ResponseEntity<List<User>>} Лист всех общих друзей двух пользователей в JSON формате.
     */
    @GetMapping("{userId}/friends/common/{friendId}")
    public ResponseEntity<List<User>> getCommonFriends(@Parameter(description = "Id одного из друзей",
            required = true) @PathVariable Integer userId, @Parameter(description = "Id другого пользователя",
            required = true) @PathVariable Integer friendId) {
        log.info("Получен GET запрос на получение общих друзей у двух юзеров");
        List<User> getAllCommonFriend = userService.getCommonFriends(userId, friendId);
        return new ResponseEntity<>(getAllCommonFriend, HttpStatus.OK);
    }

    @GetMapping("/something")
    public ResponseEntity<String> doSomething(@RequestBody User user) {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }
    // get запрос может иметь тело, но так делать нельзя

    @ExceptionHandler(CustomServerException.class)
    public ResponseEntity<?> handleCustomException(CustomServerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getClass().getName(),
                ex.getMessage()));
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<?> handleCustomException(IdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getClass().getName(),
                ex.getMessage()));
    }

    @ExceptionHandler(NotCorrectParamsException.class)
    public ResponseEntity<?> handleUserNotCorrectParamsException(NotCorrectParamsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getClass().getName(),
                ex.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, ValidationException.class})
    public ResponseEntity<?> handleValidationExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                ex.getClass().getName(),
                ex.getMessage()
        ));
    }
}
