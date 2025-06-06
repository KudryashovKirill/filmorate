package ru.yandex.practicum.filmorate.model.dto;

import javax.annotation.processing.Generated;
import ru.yandex.practicum.filmorate.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-18T13:55:18+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
public class CoolUserMapperImpl implements CoolUserMapper {

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        return userDto;
    }

    @Override
    public User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        return user;
    }
}
