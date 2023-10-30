package ru.yandex.practicum.filmorate.storage.event;


import ru.yandex.practicum.filmorate.model.feed.Event;
import java.util.List;

public interface EventStorageInterface {

    List<Event> getFeed(long userId);

    void createEvent(Event event);
}
