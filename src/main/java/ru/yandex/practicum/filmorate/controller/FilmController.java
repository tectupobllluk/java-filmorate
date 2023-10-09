package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Create film: {} - Started!", film);
        filmService.saveFilm(film);
        log.info("Create film: {} - Finished!", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Update film: {} - Started!", film);
        filmService.updateFilm(film);
        log.info("Update film: {} - Finished!", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilm(id);
    }
}
