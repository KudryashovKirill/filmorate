package ru.yandex.practicum.filmorate.model.dto;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    private UserMapper() {
    }

    public static UserDto convertToDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .login(user.getLogin())
                .friends(user.getFriends())
                .build();
    }

    public static User convertToFilm(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .birthday(userDto.getBirthday())
                .login(userDto.getLogin())
                .friends(userDto.getFriends())
                .build();
    }

    public static List<UserDto> convertFilmsToFilmsDto(List<User> users) {
        return users.stream()
                .map((UserMapper::convertToDto))
                .collect(Collectors.toList());
    }

    public static List<User> convertFilmsDtoToFilms(List<UserDto> users) {
        return users.stream()
                .map((UserMapper::convertToFilm))
                .collect(Collectors.toList());
    }
}

