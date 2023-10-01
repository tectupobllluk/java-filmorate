package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmRepository filmRepository = new FilmRepository();

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Create film: {} - Started!", film);
        filmRepository.saveFilm(film);
        log.info("Create film: {} - Finished!", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Update film: {} - Started!", film);
        filmRepository.updateFilm(film);
        log.info("Update film: {} - Finished!", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }
}
