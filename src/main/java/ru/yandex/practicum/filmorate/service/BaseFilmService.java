package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.FeedOperationEnum;
import ru.yandex.practicum.filmorate.enums.FeedTypeEnum;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;

    @Override
    public Film saveFilm(Film film) {
        return filmRepository.saveFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmRepository.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    @Override
    public void deleteFilm(long filmId) {
        filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found with id = " + filmId));

        filmRepository.deleteFilm(filmId);
    }

    @Override
    public void addLike(long userId, long filmId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        Film film = filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found with id = " + filmId));
        filmRepository.addLike(film, user);
        feedService.createFeed(userId, FeedTypeEnum.LIKE, FeedOperationEnum.ADD, filmId);
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        Film film = filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found with id = " + filmId));
        filmRepository.deleteLike(film, user);
        feedService.createFeed(userId, FeedTypeEnum.LIKE, FeedOperationEnum.REMOVE, filmId);
    }

    @Override
    public Film getFilm(long filmId) {
        return filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found with id = " + filmId));
    }

    @Override
    public List<Film> getPopularFilms(Long count, Integer genreId, Integer year) {
        return filmRepository.getPopularFilms(count, genreId, year);
    }

    @Override
    public List<Film> getAllDirectorFilms(Long directorId, String sortBy) {
        return filmRepository.getAllDirectorFilms(directorId, sortBy);
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return filmRepository.getCommonFilms(userId, friendId);
    }

    @Override
    public List<Film> searchFilms(String query, String fields) {
        return filmRepository.searchFilms(query, fields);
    }
}
