package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Feed;
import java.time.Instant;
import java.util.List;

public interface FeedRepository {

    List<Feed> getFeedList(int id);

    void updateFeed(String eventType, String operation, Long userId, Long entityId, Instant instant);

}