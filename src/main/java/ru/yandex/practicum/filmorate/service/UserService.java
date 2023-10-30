package ru.yandex.practicum.filmorate.service;

 import ru.yandex.practicum.filmorate.model.User;
 import ru.yandex.practicum.filmorate.model.feed.Event;

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

    List<Event> getFeed(int id);
}
