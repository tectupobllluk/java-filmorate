package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedRepository {

    List<Feed> getFeedList(long userId);

    void updateFeed(Feed event);

}