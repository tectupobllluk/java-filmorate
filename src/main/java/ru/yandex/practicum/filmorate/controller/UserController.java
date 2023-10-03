package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    UserRepository userRepository = new UserRepository();

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Create user: {} - Started!", user);
        userRepository.saveUser(user);
        log.info("Create user: {} - Finished!", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Update user: {} - Started!", user);
        userRepository.updateUser(user);
        log.info("Update user: {} - Finished!", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
