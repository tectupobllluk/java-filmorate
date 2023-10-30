package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.EventDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BaseFeedService implements FeedService {

    private final EventDbStorage feedStorage;

    private final UserService userService;

    public Collection<Event> getFeed(long userId) {
        userService.getUser(userId);
        return feedStorage.getFeed(userId);
    }

}