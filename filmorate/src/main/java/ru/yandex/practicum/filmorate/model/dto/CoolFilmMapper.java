package ru.yandex.practicum.filmorate.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.filmorate.model.Film;

@Mapper
public interface CoolFilmMapper {
    CoolFilmMapper INSTANCE = Mappers.getMapper(CoolFilmMapper.class);
//    @Mapping(source = "name", target = "fullName")
    FilmDto filmToFilmDto(Film film);
    Film filmDtoToFilm(FilmDto filmDto);
}
