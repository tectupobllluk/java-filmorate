package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Long id) {
        return directorService.getDirector(id);
    }

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @PostMapping
    public Director createDirector(@RequestBody @Valid Director director) {
        log.info("Create director: {} - Started!", director);
        Director newDirector = directorService.createDirector(director);
        log.info("Create director: {} - Finished!", director);
        return newDirector;
    }

    @PutMapping
    public Director updateDirector(@RequestBody @Valid Director director) {
        log.info("Update director: {} - Started!", director);
        Director updatedDirector = directorService.updateDirector(director);
        log.info("Update director: {} - Finished!", director);
        return updatedDirector;
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable Long id) {
        directorService.deleteDirector(id);
    }
}
