package ru.yandex.practicum.filmorate.model.dto;

import javax.annotation.processing.Generated;
import ru.yandex.practicum.filmorate.model.Film;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-18T13:55:18+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
public class CoolFilmMapperImpl implements CoolFilmMapper {

    @Override
    public FilmDto filmToFilmDto(Film film) {
        if ( film == null ) {
            return null;
        }

        FilmDto filmDto = new FilmDto();

        return filmDto;
    }

    @Override
    public Film filmDtoToFilm(FilmDto filmDto) {
        if ( filmDto == null ) {
            return null;
        }

        Film film = new Film();

        return film;
    }
}
