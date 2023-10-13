package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DbMpaRepositoryTest {
    private final DbMpaRepository mpaRepository;

    @Test
    void getMpa() {
        Mpa mpa = mpaRepository.getMpa(1);

        assertEquals(new Mpa(1, "G"), mpa);
    }

    @Test
    void getAllMpa() {
        List<Mpa> mpas = mpaRepository.getAllMpa();
        List<Mpa> expected = List.of(new Mpa(1, "G"), new Mpa(2, "PG"),
                new Mpa(3, "PG-13"), new Mpa(4, "R"), new Mpa(5, "NC-17"));

        assertEquals(expected, mpas);
    }
}