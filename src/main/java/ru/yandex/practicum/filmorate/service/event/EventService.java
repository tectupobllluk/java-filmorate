package ru.yandex.practicum.filmorate.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.feed.Event;
import ru.yandex.practicum.filmorate.model.feed.EventOperationEnum;
import ru.yandex.practicum.filmorate.model.feed.EventTypeEnum;
import ru.yandex.practicum.filmorate.storage.event.EventStorageInterface;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventStorageInterface eventDbStorage;

    public List<Event> getFeed(long userId) {
        return eventDbStorage.getFeed(userId);
    }

    public void createEvent(int userId, EventTypeEnum eventType, EventOperationEnum eventOperation, int entityId) {
        Event event = Event.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(eventOperation)
                .entityId(entityId)
                .build();
        eventDbStorage.createEvent(event);
    }
}