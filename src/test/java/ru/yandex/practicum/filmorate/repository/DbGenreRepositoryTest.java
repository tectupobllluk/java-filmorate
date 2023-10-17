package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DbGenreRepositoryTest {
    private final GenreRepository genreRepository;
    private final FilmRepository filmRepository;

    @Test
    void getGenre() {
        Genre genre = genreRepository.getGenre(1);

        assertEquals(new Genre(1, "Комедия"), genre);
    }

    @Test
    void getAllGenres() {
        List<Genre> genres = List.copyOf(genreRepository.getAllGenres());
        List<Genre> expected = List.of(new Genre(1, "Комедия"), new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"), new Genre(4, "Триллер"),
                new Genre(5, "Документальный"), new Genre(6, "Боевик"));

        assertEquals(expected, genres);
    }

    @Test
    void loadFilmGenre() {
        List<Genre> genres = List.of(new Genre(1, "Комедия"), new Genre(6, "Боевик"));
        filmRepository.saveFilm(new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180, new Mpa(1, "G"), genres));

        assertEquals(genres, genreRepository.loadFilmGenre(1L));
    }

    @Test
    void insertGenres() {
        List<Genre> genres = List.of(new Genre(1, "Комедия"), new Genre(6, "Боевик"));
        Film film = new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180,
                new Mpa(1, "G"), Collections.emptyList());
        filmRepository.saveFilm(film);
        filmRepository.updateFilm(new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180,
                new Mpa(1, "G"), genres));

        assertEquals(genres, genreRepository.loadFilmGenre(1L));
    }

    @Test
    void removeFilmGenres() {
        List<Genre> genres = List.of(new Genre(1, "Комедия"), new Genre(6, "Боевик"));
        Film film = new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180,
                new Mpa(1, "G"), genres);
        filmRepository.saveFilm(film);
        filmRepository.updateFilm(new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180,
                new Mpa(1, "G"), Collections.emptyList()));

        assertEquals(Collections.emptyList(), genreRepository.loadFilmGenre(1L));
    }
}