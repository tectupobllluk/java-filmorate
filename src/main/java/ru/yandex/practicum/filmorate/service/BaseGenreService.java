package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseGenreService implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Genre getGenre(int mpaId) {
        return genreRepository.getGenre(mpaId);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }
}
