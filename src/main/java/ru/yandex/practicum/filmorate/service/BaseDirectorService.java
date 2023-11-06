package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseDirectorService implements DirectorService {
    private final DirectorRepository directorRepository;

    @Override
    public Director getDirector(Long id) {
        return directorRepository.getDirector(id)
                .orElseThrow(() -> new NotFoundException("Director not found with id = " + id));
    }

    @Override
    public List<Director> getAllDirectors() {
        return directorRepository.getAllDirectors();
    }

    @Override
    public Director createDirector(Director director) {
        return directorRepository.createDirector(director);
    }

    @Override
    public Director updateDirector(Director director) {
        return directorRepository.updateDirector(director);
    }

    @Override
    public void deleteDirector(Long id) {
        directorRepository.getDirector(id)
                .orElseThrow(() -> new NotFoundException("Director not found with id = " + id));
        directorRepository.deleteDirector(id);
    }
}
