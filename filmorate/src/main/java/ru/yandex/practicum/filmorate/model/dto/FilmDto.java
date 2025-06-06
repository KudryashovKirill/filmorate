package ru.yandex.practicum.filmorate.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class FilmDto {
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Set<Integer> userWhoLikeFilm;
}
