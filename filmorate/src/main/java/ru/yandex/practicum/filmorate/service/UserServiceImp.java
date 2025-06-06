package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

/**
 * Сервис для работы с пользователями, использующий UserService для выполнения операций.
 * Реализует интерфейс UserService.
 *
 * @author Kirill Kydruashov
 * @version 17.0
 */
@Slf4j
@Service
public class UserServiceImp implements UserService {
    private final UserStorage userStorage;

    /**
     * Создает экземпляр UserServiceImp с внедренной зависимостью UserStorage.
     *
     * @param userStorage Объект класса, имплементирующего UserStorage.
     */
    @Autowired
    public UserServiceImp(@Qualifier(value = "userStorageImpDb") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Метод, в котором дублируется реализация метода создания пользователя.
     *
     * @param user Пользователь, которого нужно создать.
     * @return User Созданный пользователь.
     */
    @Override
    public User create(User user) {
        log.info("Создание юзера в сервисе");
        return userStorage.create(user);
    }

    /**
     * Метод, в котором дублируется реализация метода обновление пользователя.
     *
     * @param user Пользователь, которого нужно обновить.
     * @return User Обновленный пользователь.
     */
    @Override
    public User update(User user) {
        log.info("Обновление юзера в сервисе");
        return userStorage.update(user);
    }

    /**
     * Метод, в котором дублируется реализация метода получения всех пользователей.
     *
     * @return {@code List<User>} Лист со всеми пользователями.
     */
    @Override
    public List<User> getAllUsers() {
        log.info("Получение всех юзеров в сервисе");
        return userStorage.getAllUsersList();
    }

    /**
     * Метод, в котором дублируется реализация метода добавления в друзья.
     *
     * @param userId   Id одного из пользователей, который обоюдно добавляет пользователя и добавляется к другому
     *                 пользователю.
     * @param friendId Id другого пользователя, который обоюдно добавляет пользователя и добавляется к другому
     *                 пользователю.
     */
    @Override
    public void addFriend(Integer userId, Integer friendId) {
        log.info("Добавление в сервисе друга для юзера {} друга {}", userId, friendId);
        userStorage.addFriend(userId, friendId);
    }

    /**
     * Метод, в котором дублируется реализация метода удаления из друзей.
     *
     * @param userId   Id одного из пользователей, который обоюдно удаляет пользователя из друзей и удаляется от другого
     *                 пользователя.
     * @param friendId Id другого пользователя, который обоюдно удаляет пользователя из друзей и удаляется от другого
     *                 пользователя.
     */
    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        log.info("Удаление в сервисе друга для юзера {} друга {}", userId, friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    /**
     * Метод, в котором дублируется реализация метода получения всех друзей.
     *
     * @param userId Id пользователя, у которого нужно получить всех друзей.
     * @return {@code List<User>} Лист всех друзей пользователя.
     */
    @Override
    public List<User> getAllFriends(Integer userId) {
        log.info("Получение всех друзей в сервисе у юзера {}", userId);
        return userStorage.getAllFriends(userId);
    }

    /**
     * Метод, в котором дублируется реализация метода получения общих друзей двух пользователей.
     *
     * @param userId   Id одного из пользователей, у которого нужно получить общих друзей с другим пользователем.
     * @param friendId Id другого пользователя, у которого нужно получить общих друзей с другим пользователем.
     * @return {@code List<User>} Лист всех общих друзей пользователей.
     */
    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        log.info("Получение всех общих друзей в сервисе у юзера {} и друга{}", userId, friendId);
        return userStorage.getCommonFriends(userId, friendId);
    }
}
