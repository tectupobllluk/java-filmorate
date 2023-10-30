package ru.yandex.practicum.filmorate.model.feed;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Event {

    private int eventId;
    private long timestamp;
    private int userId;
    private EventTypeEnum eventType;
    private EventOperationEnum operation;
    private int entityId;
}