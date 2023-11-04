package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {

    Director getDirector(Long id);

    List<Director> getAllDirectors();

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(Long id);
}
