package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

public class UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private long generatorId = 0;

    private long generateId() {
        return ++generatorId;
    }

    public void saveUser(User user) {
        user.setId(generateId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new RuntimeException(String.format("Unknown user with " + user.getId() + " id!"));
        }
    }

    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }
}
