package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public class FilmRepository {
    private final HashMap<Long, Film> films = new HashMap<>();
    private long generatorId = 0;

    private long generateId() {
        return ++generatorId;
    }

    public void saveFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new RuntimeException(String.format("Unknown film with " + film.getId() + " id!"));
        }
    }

    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }
}
