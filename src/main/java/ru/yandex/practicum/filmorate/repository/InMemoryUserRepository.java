package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private final HashMap<Long, Set<Long>> userFriends = new HashMap<>();
    private long generatorId = 0;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public void saveUser(User user) {
        user.setId(generateId());
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new NotFoundException(String.format("Unknown user with " + user.getId() + " id!"));
        }
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public Optional<User> getUser(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void addFriend(User user, User friend) {
        Set<Long> userFriendIds = userFriends.computeIfAbsent(user.getId(), id -> new HashSet<>());
        userFriendIds.add(friend.getId());
        Set<Long> friendFriendIds = userFriends.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        friendFriendIds.add(user.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        Set<Long> userFriendIds = userFriends.computeIfAbsent(user.getId(), id -> new HashSet<>());
        userFriendIds.remove(friend.getId());
        Set<Long> friendFriendIds = userFriends.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        friendFriendIds.remove(user.getId());
    }

    @Override
    public List<User> getCommonFriends(User user, User friend) {
        Set<Long> userFriendIds = userFriends.get(user.getId());
        Set<Long> friendFriendIds = userFriends.get(friend.getId());

        if (userFriendIds == null || friendFriendIds == null) {
            return Collections.emptyList();
        }

        return userFriendIds.stream()
                .filter(friendFriendIds::contains)
                .map(users::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFriends(User user) {
        Set<Long> userFriendIds = userFriends.get(user.getId());
        return userFriendIds.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }
}
