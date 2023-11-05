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
        Film newFilm = filmService.saveFilm(film);
        log.info("Create film: {} - Finished!", film);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Update film: {} - Started!", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Update film: {} - Finished!", film);
        return updatedFilm;
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
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) Integer year) {
        return filmService.getPopularFilms(count, genreId, year);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmService.getFilm(id);
    }


    @GetMapping("/director/{directorId}")
    public List<Film> getAllDirectorFilms(@PathVariable long directorId, @RequestParam String sortBy) {
        return filmService.getAllDirectorFilms(directorId, sortBy);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "by", defaultValue = "director,title") String fields) {
        return filmService.searchFilms(query, fields);
    }
}
