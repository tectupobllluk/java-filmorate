package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.enums.FeedOperationEnum;
import ru.yandex.practicum.filmorate.enums.FeedTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class Feed {
    @NotNull
    private final long timestamp;
    @NotNull
    private final long userId;
    @NotNull
    private final FeedTypeEnum eventType;
    @NotNull
    private final FeedOperationEnum operation;
    @NotNull
    private final int eventId;
    @NotNull
    private final long entityId;
}