package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class FilmServiceTest {
    private FilmStorage interfaceFilm;
    private Film pulpFiction;
    private Film pulpFictionForUpdate;
    private Film pulpFictionUpdated;
    private Film forrestGump;

    @BeforeEach
    public void createInterface() {
        interfaceFilm = new FilmStorageImp(new UserStorageImp());
        pulpFiction = new Film("pulpFiction", "desc1", LocalDate.of(1985, 9, 29),
                149);
        pulpFictionForUpdate = new Film(1, "pulpFiction", null, LocalDate.of(1985, 9, 29),
                60);
        pulpFictionUpdated = new Film(1, "pulpFiction", "desc1",
                LocalDate.of(1985, 9, 29), 60);
        forrestGump = new Film("forestGump", "desc2", LocalDate.of(1994, 7, 6),
                144);
    }

    @Test
    public void createIfFilmIsNull() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceFilm.create(null));
        Assertions.assertEquals("film must be not null", exception.getMessage());
    }

    @Test
    public void createIfNameIsEmpty() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceFilm.create(new Film("", "desc1", LocalDate.of(1985, 9, 29),
                        149)));
        Assertions.assertEquals("name must be not null", exception.getMessage());
    }

    @Test
    public void createIfDescIsMoreThan200() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceFilm.create(new Film("pulpFiction", "ffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "f", LocalDate.of(1985, 9, 29),
                        149)));
        Assertions.assertEquals("description must be less than 200 symbols", exception.getMessage());
    }

    @Test
    public void createIfReleaseDateIsWrong() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceFilm.create(new Film("pulpFiction", "desc1",
                        LocalDate.of(1785, 9, 29), 149)));
        Assertions.assertEquals("film must be released after 28-12-1895", exception.getMessage());
    }

    @Test
    public void createIfDurationIsWrong() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceFilm.create(new Film("pulpFiction", "desc1",
                        LocalDate.of(1985, 9, 29), -4)));
        Assertions.assertEquals("duration must be positive", exception.getMessage());
    }

    @Test
    public void createIfFilm() {
        List<Film> expectedFilms = new ArrayList<>(List.of(pulpFiction));
        interfaceFilm.create(pulpFiction);
        Assertions.assertEquals(expectedFilms, interfaceFilm.getAllFilms());
    }

    @Test
    public void updateIfFilmIsNull() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceFilm.update(null));
        Assertions.assertEquals("film must be not null", exception.getMessage());
    }

    @Test
    public void updateIfFilmIsNotInAllFilms() {
        interfaceFilm.create(pulpFiction);
        forrestGump.setId(100);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                interfaceFilm.update(forrestGump));
        Assertions.assertEquals("no films with that id", exception.getMessage());
    }

    @Test
    public void update() {
        interfaceFilm.create(pulpFiction);
        interfaceFilm.update(pulpFictionForUpdate);
        List<Film> expectedFilms = new ArrayList<>(List.of(pulpFictionUpdated));
        Assertions.assertEquals(expectedFilms, interfaceFilm.getAllFilms());
    }

    @Test
    public void getAllFilms() {
        interfaceFilm.create(pulpFiction);
        interfaceFilm.create(forrestGump);
        List<Film> films = new ArrayList<>(List.of(pulpFiction,forrestGump));
        Assertions.assertEquals(films, interfaceFilm.getAllFilms());
    }
}