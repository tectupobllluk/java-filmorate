package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Film saveFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilm(long filmId);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getPopularFilms(Long count);

    List<Film> getCommonFilms(Long userId, Long friendId);

}
