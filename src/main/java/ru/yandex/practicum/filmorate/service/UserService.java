package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserService {

    void saveUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    User getUser(long userId);

    void addFriend(long friendId, long userId);

    void deleteFriend(long friendId, long userId);

    List<User> getCommonFriends(long friendId, long userId);

    List<User> getFriends(long userId);

    List<Film> getFilmRecommendations(long userId);

    void deleteUser(long userId);

    List<Feed> getFeed(long userId);


}
