package ru.yandex.practicum.filmorate.model.dto;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;
public class FilmMapper {
    private FilmMapper() {
    }

    public static FilmDto convertToDto(Film film) {
        return FilmDto.builder()
                .userWhoLikeFilm(film.getUserWhoLikeFilm())
                .name(film.getName())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .description(film.getDescription())
                .build();
    }

    public static Film convertToFilm(FilmDto filmDto) {
        return Film.builder()
                .userWhoLikeFilm(filmDto.getUserWhoLikeFilm())
                .name(filmDto.getName())
                .releaseDate(filmDto.getReleaseDate())
                .description(filmDto.getDescription())
                .duration(filmDto.getDuration())
                .build();
    }

    public static List<FilmDto> convertFilmsToFilmsDto(List<Film> films) {
        return films.stream()
                .map((FilmMapper::convertToDto))
                .collect(Collectors.toList());
    }

    public static List<Film> convertFilmsDtoToFilms(List<FilmDto> films) {
        return films.stream()
                .map((FilmMapper::convertToFilm))
                .collect(Collectors.toList());
    }
}

