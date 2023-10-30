package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void saveUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    Optional<User> getUser(long userId);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    void deleteUser(long userId);

    List<User> getCommonFriends(User user, User friend);

    List<User> getFriends(User user);
}
