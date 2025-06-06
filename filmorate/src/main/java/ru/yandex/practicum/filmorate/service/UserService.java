package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
/**
 * Интерфейс, имеющий те же методы, что и UserStorage, но на уровне сервиса.
 * @author Kirill Kydruashov
 * @version 17.0
 */
public interface UserService {
    User create(User user);
    User update(User user);
    List<User> getAllUsers();
    void addFriend(Integer userId, Integer friendId);
    void deleteFriend(Integer userId, Integer friendId);
    List<User> getAllFriends(Integer userId);
    List<User> getCommonFriends(Integer userId, Integer friendId);
}
