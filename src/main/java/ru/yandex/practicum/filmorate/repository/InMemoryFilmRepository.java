package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmRepository implements FilmRepository {
    private final HashMap<Long, Film> films = new HashMap<>();
    private final HashMap<Long, Set<Long>> filmLikes = new HashMap<>();
    private long generatorId = 0;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public void saveFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new NotFoundException(String.format("Unknown film with " + film.getId() + " id!"));
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Optional<Film> getFilm(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public void addLike(Film film, User user) {
        Set<Long> filmUserLikes = filmLikes.computeIfAbsent(film.getId(), id -> new HashSet<>());
        filmUserLikes.add(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        Set<Long> filmUserLikes = filmLikes.computeIfAbsent(film.getId(), id -> new HashSet<>());
        filmUserLikes.remove(user.getId());
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return films.values().stream()
                .sorted(new FilmComparator())
                .limit(count).collect(Collectors.toList());
    }

    class FilmComparator implements Comparator<Film> {

        @Override
        public int compare(Film firstFilm, Film secondFilm) {
            Set<Long> firstFilmLikes = filmLikes.get(firstFilm.getId());
            Set<Long> secondFilmLikes = filmLikes.get(secondFilm.getId());
            int firstLikesCount = 0;
            int secondLikesCount = 0;
            if (firstFilmLikes != null) {
                firstLikesCount = firstFilmLikes.size();
            }
            if (secondFilmLikes != null) {
                secondLikesCount = secondFilmLikes.size();
            }
            return secondLikesCount - firstLikesCount;
        }
    }
}