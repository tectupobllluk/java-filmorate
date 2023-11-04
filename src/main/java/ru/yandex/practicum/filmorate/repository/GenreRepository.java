package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;

public interface GenreRepository {
    Genre getGenre(int genreId);

    List<Genre> getAllGenres();

    List<Genre> loadFilmGenre(Long filmId);

    void insertGenres(Film film);

    void removeFilmGenres(Film film);
}
