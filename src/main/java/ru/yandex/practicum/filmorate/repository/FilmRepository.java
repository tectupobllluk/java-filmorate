package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    void saveFilm(Film film);

    void updateFilm(Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilm(long filmId);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getPopularFilms(Long count);
}
