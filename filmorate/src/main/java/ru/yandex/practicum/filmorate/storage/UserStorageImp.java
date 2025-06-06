package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.CustomServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.util.UserValidator;

import java.util.*;

/**
 * Класс, реализующий методы интерфейса UserStorage Содержит idGenerator для автоматической генерации id пользователей,
 * мапу Integer, User, хранящую пользователя и его id.
 * @author Kirill Kydruashov
 * @version 17.0
 */
@Component
public class UserStorageImp implements UserStorage {
    private static int idGenerator = 1;
    @Getter
    private Map<Integer, User> allUsers = new HashMap<>();

    /**
     * Метод, валидирующий пользователя по его параметрам, помещающий корректный объект пользователя в мапу хранения
     * и присваивающий ему id.
     *
     * @param user объект пользователя, который нужно добавить в мапу и присвоить id.
     * @return User Созданный пользователь.
     * @throws CustomServerException Ошибка в параметрах объекта.
     * @throws IllegalArgumentException                                 Выпадает, если user == null.
     */
    @Override
    public User create(User user) {
        UserValidator.validationAllFields(user);
        user.setId(idGenerator);
        allUsers.put(idGenerator++, user);
        return user;
    }

    /**
     * Метод, предназначенный для патчинга пользователя внутри мапы.
     *
     * @param user объект пользователя, предназначенный для патчинга с пользователем из мапы с таким же id
     * @return User Обновленный пользователь.
     * @throws IllegalArgumentException выпадает при условии, что user == null.
     * @throws ru.yandex.practicum.filmorate.exception.IdNotFoundException     выпадает, если пользователя с id, переданного в параметры метода, нет в мапе хранения.
     */
    @Override
    public User update(User user) {
        UserValidator.validationAllFields(user);
        User currentUser = allUsers.get(user.getId());
        if (currentUser == null) {
            throw new IdNotFoundException("Пользователя с таким id не существует");
        }
        if (user.getEmail() != null) {
            currentUser.setEmail(user.getEmail());
        }
        if (user.getLogin() != null) {
            currentUser.setLogin(user.getLogin());
        }
        if (user.getName() != null) {
            currentUser.setName(user.getName());
        }
        if (user.getBirthday() != null) {
            currentUser.setBirthday(user.getBirthday());
        }
        return currentUser;
    }

    /**
     * Метод для обоюдного добавления в друзья двух пользователей.
     *
     * @param userId   Id пользователя, в список друзей которого нужно добавить пользователя с friendId.
     * @param friendId Id друга, которого нужно добавить в друзья к пользователю userId. И к которому следует добавить
     *                 в его список друзей пользователя с userId.
     */
    @Override
    public void addFriend(Integer userId, Integer friendId) {
        validationIsInAllUsersUser(userId);
        validationIsInAllUsersFriend(friendId);

        allUsers.get(userId).getFriends().add(friendId);
        allUsers.get(friendId).getFriends().add(userId);
    }

    /**
     * Метод для получения всех друзей у пользователя.
     *
     * @param userId Id пользователя, у которого нужно получить всех друзей.
     * @return {@code List<User>} Лист всех друзей пользователя.
     * @throws ru.yandex.practicum.filmorate.exception.IdNotFoundException Выпадает в случае, когда пользователя с переданным в параметрах userId нет в мапе
     *                              хранения.
     */
    @Override
    public List<User> getAllFriends(Integer userId) {
        validationIsInAllUsersUser(userId);
        List<User> result = new ArrayList<>();

        for (Integer id : allUsers.get(userId).getFriends()) {
            result.add(allUsers.get(id));
        }
        return result;
    }

    /**
     * Метод для удаления друга у пользователя.
     *
     * @param userId   Id пользователя, у которого нужно удалить друга.
     * @param friendId Id друга, которого нужно удалить из друзей у пользователя с userId. И у которого следует удалить
     *                 в его список друзей пользователя с userId.
     * @throws IdNotFoundException Выпадает в случае, когда пользователя с переданным в параметрах userId нет в мапе
     *                              хранения.
     * @throws IdNotFoundException Выпадает в случае, когда пользователя с переданным в параметрах friendId нет в мапе
     *                              хранения.
     */
    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        validationIsInAllUsersUser(userId);
        validationIsInAllUsersFriend(friendId);

        allUsers.get(userId).getFriends().remove(friendId);
        allUsers.get(friendId).getFriends().remove(userId);
    }

    /**
     * Метод, преобразующий значения мапы в лист всех пользователей.
     * @return {@code List<User>} лист со всеми пользователями.
     */
    @Override
    public List<User> getAllUsersList() {
        return new ArrayList<>(allUsers.values());
    }

    /**
     * Метод для получения общих друзей двух пользователей.
     *
     * @param userId   Id одного из пользователей, у которого следует получить общих друзей.
     * @param friendId Id другого пользователя, у которого нужно получить общих друзей с пользователем userId.
     * @return {@code List<User>} Список общих друзей двух пользователей.
     * @throws IdNotFoundException Выпадает в случае, когда пользователя с переданным в параметрах userId нет в мапе
     *                              хранения.
     * @throws IdNotFoundException Выпадает в случае, когда пользователя с переданным в параметрах friendId нет в мапе
     *                              хранения.
     */
    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        validationIsInAllUsersUser(userId);
        validationIsInAllUsersFriend(friendId);

        Set<Integer> user1Set = new HashSet<>(allUsers.get(userId).getFriends());
        user1Set.retainAll(allUsers.get(friendId).getFriends());
        List<User> users = new ArrayList<>();

        for (Integer commonFriendId : user1Set) {
            users.add(allUsers.get(commonFriendId));
        }
        return users;
    }

    @Override
    public User getById(Integer id) {
        validationIsInAllUsersUser(id);
        return allUsers.get(id);
    }

    private void validationIsInAllUsersUser(Integer userId) {
        if (!allUsers.containsKey(userId)) {
            throw new IdNotFoundException("no user with that id");
        }
    }

    private void validationIsInAllUsersFriend(Integer friendId) {
        if (!allUsers.containsKey(friendId)) {
            throw new IdNotFoundException("no friend with that id");
        }
    }
}
