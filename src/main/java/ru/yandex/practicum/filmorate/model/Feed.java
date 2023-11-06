package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.enums.FeedOperationEnum;
import ru.yandex.practicum.filmorate.enums.FeedTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Feed {
    private final long timestamp;
    private final long userId;
    private final FeedTypeEnum eventType;
    private final FeedOperationEnum operation;
    private final int eventId;
    private final long entityId;
}