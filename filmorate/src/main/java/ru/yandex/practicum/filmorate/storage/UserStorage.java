package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
/**
 * Интерфейс с методами всей бизнес логики для сущности пользователь приложения Filmorate
 * (спросить про это)
 * Данные методы позволяют
 * создать и обновить пользователя, получить их всех, добавить пользователя в друзья и удалить его,
 * получить всех друзей пользователя и общих с другим пользователем.
 * @author Kirill Kydryashov
 * @version 17.0
 */
public interface UserStorage {
    User create(User user);
    User update(User user);
    User getById(Integer id);
    List<User> getAllUsersList();
    void addFriend(Integer userId, Integer friendId);
    void deleteFriend(Integer userId, Integer friendId);
    List<User> getAllFriends(Integer userId);
    List<User> getCommonFriends(Integer userId, Integer friendId);
}
