package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validateCorrectFilm() {
        Film film = new Film(1L, "Test", "test test test",
                LocalDate.of(2000, 2, 9), 180);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "Normal formatted film");
    }

    @Test
    void validateEmptyName() {
        Film film = new Film(1L, "", "test test test",
                LocalDate.of(2000, 2, 9), 180);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Empty name of film");
    }

    @Test
    void validateSizeOfDescription() {
        String description = "testtesttesttesttesttesttesttesttesttest" +
                "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest" +
                "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
        Film film = new Film(1L, "Test", description,
                LocalDate.of(2000, 2, 9), 180);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(204, description.length());
        assertEquals(1, violations.size(), "Description length more than 200 symbols");
    }

    @Test
    void validateReleaseDate() {
        Film film = new Film(1L, "Test", "test test test",
                LocalDate.of(1890, 2, 9), 180);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Release date before 28 december 1895");
    }

    @Test
    void validateDurationOfFilm() {
        Film film = new Film(1L, "Test", "test test test",
                LocalDate.of(2000, 2, 9), -180);
        Film secondFilm = new Film(1L, "Test", "test test test",
                LocalDate.of(2000, 2, 9), 0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Negative duration of film");
        Set<ConstraintViolation<Film>> secondViolations = validator.validate(secondFilm);
        assertEquals(1, secondViolations.size(), "Zero duration of film");
    }
}
