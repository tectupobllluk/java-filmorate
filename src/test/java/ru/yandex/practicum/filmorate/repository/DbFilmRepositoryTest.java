package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DbFilmRepositoryTest {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Test
    void saveFilm() {
        Mpa mpa = new Mpa(1, "G");

        filmRepository.saveFilm(new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180, mpa, Collections.emptyList()));
        Optional<Film> filmOptional = filmRepository.getFilm(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "name"));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "description"));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(2000, 2, 9)));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 180));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("mpa", mpa));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", Collections.emptyList()));
    }

    @Test
    void updateFilm() {
        Mpa mpa = new Mpa(1, "G");
        Mpa newMpa = new Mpa(2, "PG");

        filmRepository.saveFilm(new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180, mpa, Collections.emptyList()));
        filmRepository.updateFilm(new Film(1L, "newName", "newDescription",
                LocalDate.of(2029, 2, 9), 360, newMpa, Collections.emptyList()));
        Optional<Film> newFilmOptional = filmRepository.getFilm(1);

        assertThat(newFilmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
        assertThat(newFilmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "newName"));
        assertThat(newFilmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "newDescription"));
        assertThat(newFilmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(2029, 2, 9)));
        assertThat(newFilmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 360));
        assertThat(newFilmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("mpa", newMpa));
        assertThat(newFilmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", Collections.emptyList()));
    }

    @Test
    void getAllFilms() {
        Mpa mpa = new Mpa(1, "G");
        Mpa newMpa = new Mpa(2, "PG");

        Film film = new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180, mpa, Collections.emptyList());
        filmRepository.saveFilm(film);
        Film secondFilm = new Film(2L, "newName", "newDescription",
                LocalDate.of(2029, 2, 9), 360, newMpa, Collections.emptyList());
        filmRepository.saveFilm(secondFilm);
        List<Film> filmList = filmRepository.getAllFilms();

        assertEquals(2, filmList.size());
        assertEquals(film, filmList.get(0));
        assertEquals(secondFilm, filmList.get(1));
    }

    @Test
    void getFilm() {
        Mpa mpa = new Mpa(1, "G");

        filmRepository.saveFilm(new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180, mpa, Collections.emptyList()));
        Optional<Film> filmOptional = filmRepository.getFilm(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "name"));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "description"));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                LocalDate.of(2000, 2, 9)));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 180));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("mpa", mpa));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("genres", Collections.emptyList()));
    }

    @Test
    void addLike() {
        User user = new User(1L, "email@ru.ru", "login", "nme",
                LocalDate.of(2000, 2, 9));
        userRepository.saveUser(user);

        Mpa mpa = new Mpa(1, "G");
        Mpa newMpa = new Mpa(2, "PG");

        Film firstFilm = new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180, mpa, Collections.emptyList());
        filmRepository.saveFilm(firstFilm);
        Film secondFilm = new Film(2L, "newName", "newDescription",
                LocalDate.of(2020, 2, 9), 360, newMpa, Collections.emptyList());
        filmRepository.saveFilm(secondFilm);
        filmRepository.addLike(secondFilm, user);
        List<Film> filmList = filmRepository.getPopularFilms(1L);

        assertEquals(1, filmList.size());
        assertEquals(secondFilm, filmList.get(0));
    }

    @Test
    void deleteLike() {
        User user = new User(1L, "email@ru.ru", "login", "nme",
                LocalDate.of(2000, 2, 9));
        userRepository.saveUser(user);

        Mpa mpa = new Mpa(1, "G");
        Mpa newMpa = new Mpa(2, "PG");

        Film firstFilm = new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180, mpa, Collections.emptyList());
        filmRepository.saveFilm(firstFilm);
        Film secondFilm = new Film(2L, "secondName", "secondDescription",
                LocalDate.of(2020, 2, 9), 360, newMpa, Collections.emptyList());
        filmRepository.saveFilm(secondFilm);
        filmRepository.addLike(secondFilm, user);
        filmRepository.deleteLike(secondFilm, user);
        List<Film> filmList = filmRepository.getPopularFilms(1L);

        assertEquals(1, filmList.size());
        assertEquals(firstFilm, filmList.get(0));
    }

    @Test
    void getPopularFilms() {
        User user = new User(1L, "email@ru.ru", "login", "nme",
                LocalDate.of(2000, 2, 9));
        userRepository.saveUser(user);
        User secondUser = new User(2L, "emailTwo@ru.ru", "loginTwo", "",
                LocalDate.of(2020, 2, 9));
        userRepository.saveUser(secondUser);

        Mpa mpa = new Mpa(1, "G");
        Mpa secondMpa = new Mpa(3, "PG-13");
        Mpa thirdMpa = new Mpa(2, "PG");

        Film firstFilm = new Film(1L, "name", "description",
                LocalDate.of(2000, 2, 9), 180, mpa, Collections.emptyList());
        filmRepository.saveFilm(firstFilm);
        Film secondFilm = new Film(2L, "secondName", "secondDescription",
                LocalDate.of(2020, 2, 9), 360, secondMpa, Collections.emptyList());
        filmRepository.saveFilm(secondFilm);
        Film thirdFilm = new Film(3L, "thirdName", "thirdDescription",
                LocalDate.of(2010, 2, 9), 200, thirdMpa, Collections.emptyList());
        filmRepository.saveFilm(thirdFilm);
        filmRepository.addLike(thirdFilm, user);
        filmRepository.addLike(thirdFilm, secondUser);
        filmRepository.addLike(secondFilm, user);
        List<Film> filmList = filmRepository.getPopularFilms(3L);

        assertEquals(3, filmList.size());
        assertEquals(thirdFilm, filmList.get(0));
        assertEquals(secondFilm, filmList.get(1));
        assertEquals(firstFilm, filmList.get(2));
    }
}