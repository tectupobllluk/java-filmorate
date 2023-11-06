package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.enums.FeedOperationEnum;
import ru.yandex.practicum.filmorate.enums.FeedTypeEnum;
import ru.yandex.practicum.filmorate.model.Feed;
import java.util.List;

public interface FeedService {

    List<Feed> getFeedList(long id);

    void createFeed(long userId, FeedTypeEnum eventType, FeedOperationEnum eventOperation, long entityId);
}
