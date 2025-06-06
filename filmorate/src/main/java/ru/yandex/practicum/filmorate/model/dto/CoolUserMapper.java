package ru.yandex.practicum.filmorate.model.dto;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.model.User;

@Mapper
public interface CoolUserMapper {
    CoolUserMapper INSTANCE = Mappers.getMapper(CoolUserMapper.class);
    UserDto userToUserDto(User user);
//    @InheritConfiguration(name = "userToUserDto")
    User userDtoToUser(UserDto userDto);
}
