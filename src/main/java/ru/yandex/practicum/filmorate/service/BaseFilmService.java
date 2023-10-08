package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public void saveFilm(Film film) {
        filmRepository.saveFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        filmRepository.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    @Override
    public void addLike(long userId, long filmId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        Film film = filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found with id = " + filmId));
        filmRepository.addLike(film, user);
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        Film film = filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found with id = " + filmId));
        filmRepository.deleteLike(film, user);
    }

    @Override
    public Film getFilm(long filmId) {
        return filmRepository.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found with id = " + filmId));
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        if (count == null) {
            count = 10L;
        }
        return filmRepository.getPopularFilms(count);
    }
}
