package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.enums.EventOperationEnum;
import ru.yandex.practicum.filmorate.enums.EventTypeEnum;

public interface FeedSaveDB {
    void saveEvent(long userId, int eventType, int operationType, long entityId);

    int getEventTypeId(EventTypeEnum eventType);

    int getOperationTypeId(EventOperationEnum operationType);
}
