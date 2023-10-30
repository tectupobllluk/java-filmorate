package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

public interface FeedService {
    public Collection<Event> getFeed(long id);
}
