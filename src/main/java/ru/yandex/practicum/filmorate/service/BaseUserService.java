package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseUserService implements UserService {

    private final UserRepository userRepository;

    private final FilmRepository filmRepository;

    @Override
    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUser(long userId) {
        return userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
    }

    @Override
    public void addFriend(long friendId, long userId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        User friend = userRepository.getUser(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found with id = " + friendId));
        userRepository.addFriend(user, friend);
    }

    @Override
    public void deleteFriend(long friendId, long userId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        User friend = userRepository.getUser(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found with id = " + friendId));
        userRepository.deleteFriend(user, friend);
    }

    @Override
    public List<User> getCommonFriends(long friendId, long userId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        User friend = userRepository.getUser(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found with id = " + friendId));
        return userRepository.getCommonFriends(user, friend);
    }

    @Override
    public List<User> getFriends(long userId) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        return userRepository.getFriends(user);
    }

    @Override
    public List<Film> getFilmRecommendations(int userId) {
        User user =  this.getUser(userId);
        return filmRepository.getRecommendedFilms(user.getId());

    }
}
