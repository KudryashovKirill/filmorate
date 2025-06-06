package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class UserServiceTest {
    private UserStorage interfaceUser;
    private User user1;
    private User user2;
    private User user3;
    private User user4;

    @BeforeEach
    public void createObjects() {
        interfaceUser = new UserStorageImp();
        user1 = new User("email@gmail.com", "login1", "Kirill",
                LocalDate.of(2005, 1, 14));
        user2 = new User(1, "email@gmail.com", "login2", "Kirill", null);
        user3 = new User(1, "email@gmail.com", "login2", "Kirill",
                LocalDate.of(2005, 1, 14));
        user4 = new User("email2@gmail.com", "login4", "Sasha",
                LocalDate.of(2025, 1, 22));
    }

    @Test
    public void createIfUserIsNull() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceUser.create(null));
        Assertions.assertEquals("user must be not null", exception.getMessage());
    }

    @Test
    public void createIfEmailIsIsWrong() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceUser.create(new User("", "login1", "Kirill",
                        LocalDate.of(2005, 1, 14))));
        Assertions.assertEquals("email must be right", exception.getMessage());
    }

    @Test
    public void createIfLoginIsWrong() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceUser.create(new User("email@gmail.com", "", "Kirill",
                        LocalDate.of(2005, 1, 14))));
        Assertions.assertEquals("login must be right", exception.getMessage());
    }

    @Test
    public void createIfNameIsNull() {
        User user5 = new User("email@gmail.com", "Login", "",
                LocalDate.of(2005, 1, 14));
        interfaceUser.create(user5);
        Assertions.assertEquals("Login", user5.getName());
    }

    @Test
    public void createIfBirthdayIsWrong() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceUser.create(new User("email@gmail.com", "Login", "Kirill",
                        LocalDate.of(2026, 1, 14))));
        Assertions.assertEquals("birthday must be not in future", exception.getMessage());
    }

    @Test
    public void create() {
        interfaceUser.create(user1);
        interfaceUser.create(user4);
        List<User> expectedUsers = new ArrayList<>(List.of(user1, user4));
        Assertions.assertEquals(expectedUsers, interfaceUser.getAllUsersList());
    }

    @Test
    public void updateIfUserIsNull() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceUser.update(null));
        Assertions.assertEquals("user must be not null", exception.getMessage());
    }

    @Test
    public void updateIfUserIsNotInAllUsers() {
        interfaceUser.create(user1);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceUser.update(user4));
        Assertions.assertEquals("no user with that id", exception.getMessage());
    }
    @Test
    public void update(){
        interfaceUser.create(user1);
        interfaceUser.update(user2);
        List<User> expectedUsers = new ArrayList<>(List.of(user3));
        Assertions.assertEquals(expectedUsers, interfaceUser.getAllUsersList());
    }
    @Test
    public void getAllUsers(){
        interfaceUser.create(user1);
        interfaceUser.create(user4);
        List<User> expectedUsers = new ArrayList<>(List.of(user1,user4));
        Assertions.assertEquals(expectedUsers, interfaceUser.getAllUsersList());
    }
}