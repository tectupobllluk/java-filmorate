package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film saveFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    void deleteFilm(long filmId);

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    Film getFilm(long filmId);

    List<Film> getPopularFilms(Long count);

    List<Film> getAllDirectorFilms(Long directorId, String sortBy);

    List<Film> searchFilms(String query, String fields);
}
