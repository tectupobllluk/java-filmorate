package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Create user: {} - Started!", user);
        userService.saveUser(user);
        log.info("Create user: {} - Finished!", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Update user: {} - Started!", user);
        userService.updateUser(user);
        log.info("Update user: {} - Finished!", user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToUser(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendFromUser(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(friendId, id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(otherId, id);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getFilmRecommendations(@PathVariable long id) {
        return userService.getFilmRecommendations(id);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getFeed(@PathVariable int id) {
        return userService.getFeed(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
