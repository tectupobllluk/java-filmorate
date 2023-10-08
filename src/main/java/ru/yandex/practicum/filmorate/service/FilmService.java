package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    void saveFilm(Film film);

    void updateFilm(Film film);

    List<Film> getAllFilms();

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    Film getFilm(long filmId);

    List<Film> getPopularFilms(Long count);
}
