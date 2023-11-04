package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Feed {
    private final long timestamp;
    private final int userId;
    private final String eventType;
    private final String operation;
    private final int eventId;
    private final int entityId;
}