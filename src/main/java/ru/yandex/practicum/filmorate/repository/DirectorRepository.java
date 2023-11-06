package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface DirectorRepository {
    List<Director> loadFilmDirector(Long filmId);

    Optional<Director> getDirector(Long id);

    List<Director> getAllDirectors();

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(Long id);

    void insertDirectors(Film film);

    void removeFilmDirectors(Film film);
}
