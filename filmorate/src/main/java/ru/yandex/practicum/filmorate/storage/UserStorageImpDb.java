package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotCorrectParamsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class UserStorageImpDb implements UserStorage {
    JdbcTemplate jdbcTemplate;
    SimpleJdbcInsert insertForUsers;
    SimpleJdbcInsert insertForFriends;

    @Autowired
    public UserStorageImpDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertForUsers = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.insertForFriends = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("friends");
    }

    @Override
    public User create(User user) { // у юзера сделать метод toMap переводящий все поля в мапу
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") || user.getLogin().contains(" ")
            || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("неправильные данные юзера");
            throw new NotCorrectParamsException("");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());

        Number id = insertForUsers.executeAndReturnKey(params);
        user.setId(id.intValue());
        log.info("Создан пользователь {} и добавлен в таблицу", user);
        return user;
    }

    @Override
    public User update(User user) {//todo дописать
        if (user == null) {
            log.error("Передан null вместо пользователя");
            throw new IllegalArgumentException("User cannot be null");
        }

        String sqlForUserExists = "SELECT COUNT(*) FROM users WHERE id = ?";
        boolean userExists = jdbcTemplate.queryForObject(
                sqlForUserExists,
                Integer.class,
                user.getId()
        ) > 0;

        if (!userExists) {
            log.error("Пользователь с id {} не найден", user.getId());
            throw new IdNotFoundException("User not found");
        }

        String sqlQuery = """
                UPDATE users
                SET email = ?, login = ?, name = ?, birthday = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        log.info("Пользователь запатчен {}", user);
        return user;
    }

    @Override
    public List<User> getAllUsersList() {
        String sqlQuery = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getTimestamp("birthday").toLocalDateTime().toLocalDate()
                ));
        log.info("Получены все фильмы из таблицы users");
        return users;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) { // todo переделать
        if (!userExists(userId) || !userExists(friendId)) {
            log.error("Невозможно добавить друзей {}, {} тк их нет в таблице users", userId, friendId);
            throw new IdNotFoundException("");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("friend_id", friendId);
        insertForFriends.execute(params);
        log.info("добавлен в таблицу \"друзья\" друг {} к юзеру {}", friendId, userId);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("user_id", friendId);
        params2.put("friend_id", userId);
        insertForFriends.execute(params2);
        log.info("добавлен в таблицу \"друзья\" друг {} к юзеру {}", userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        if (!userExists(userId) || !userExists(friendId)) {
            log.error("в таблице users нет пользователя {} или друга {}", userId, friendId);
            throw new IdNotFoundException("");
        }

        String sqlQueryCheckFriendship = "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";
        int count = jdbcTemplate.queryForObject(sqlQueryCheckFriendship, Integer.class, userId, friendId);
        if (count == 0) {
            log.error("Дружбы между {} и {} не существует", userId, friendId);
            return;
        }

        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        if (jdbcTemplate.update(sqlQuery, userId, friendId) != 0) {
            log.info("Удален друг {} из таблицы \"friends\" у юзера {}", friendId, userId);
        } else {
            log.info("Нет пары юзер - друг по переданным id {}, {}", userId, friendId);
            throw new IllegalArgumentException();
        }

        if ((jdbcTemplate.update(sqlQuery, friendId, userId) != 0)) {
            log.info("Удален друг {} из таблицы \"friends\" у юзера {}", userId, friendId);
        } else {
            log.info("Нет пары юзер - друг по переданным id {}, {}", friendId, userId);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public User getById(Integer id) { // todo
        try {
            String sqlQuery = "SELECT * FROM users WHERE id = ?";
            User user = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> new User(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getTimestamp("birthday").toLocalDateTime().toLocalDate()
            ));
            log.info("Получен юзер {}", user);
            return user;
        } catch (Exception e) {
            log.error("Пользователь с таким id = {} не найден", id);
            throw new UserNotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        if (!userExists(userId)) {
            log.error("Невозможно получить друзей у пользователя с id {} тк его нет в таблице users", userId);
            throw new IdNotFoundException("");
        }
        String sqlQuery = """
                SELECT * FROM users 
                JOIN friends ON users.id = friends.friend_id
                WHERE friends.user_id = ?
                """;
        List<User> users = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new User(
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getTimestamp("birthday").toLocalDateTime().toLocalDate()
                ), userId);
        log.info("Получены друзья юзера {}", userId);
        return users;
    }
    // избегать обращение к бд в цикле

    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        String sqlQuery = """
                SELECT u.* FROM users u
                WHERE u.id IN (
                    SELECT friend_id FROM friends WHERE user_id = ?
                    INTERSECT
                    SELECT friend_id FROM friends WHERE user_id = ?
                )
                """;
        List<User> users = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getTimestamp("birthday").toLocalDateTime().toLocalDate()
                ), userId, friendId);
        log.info("Получены общие друзья юзера {} и юзера {}", userId, friendId);
        return users;
    }

    private boolean userExists(Integer id) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count > 0;
    }
}
// сделать запрос который будет возвращать именно пересечения(юзеров, которые являются друзьями обоих юзеров)

