package ru.yandex.practicum.filmorate.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class UserDto {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(email, userDto.email) && Objects.equals(login, userDto.login)
               && Objects.equals(name, userDto.name) && Objects.equals(birthday, userDto.birthday)
               && Objects.equals(friends, userDto.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, login, name, birthday, friends);
    }
}
