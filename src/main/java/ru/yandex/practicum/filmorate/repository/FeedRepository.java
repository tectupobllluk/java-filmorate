package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.EventOperationEnum;
import ru.yandex.practicum.filmorate.model.EventTypeEnum;

public interface FeedRepository {
    void saveEvent(long userId, int eventType, int operationType, long entityId);

    int getEventTypeId(EventTypeEnum eventType);

    int getOperationTypeId(EventOperationEnum operationType);
}
