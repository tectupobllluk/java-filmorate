package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.FeedOperationEnum;
import ru.yandex.practicum.filmorate.enums.FeedTypeEnum;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.repository.FeedRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseFeedService implements FeedService {

    private final FeedRepository feedRepository;

    @Override
    public List<Feed> getFeedList(long id) {
        return feedRepository.getFeedList(id);
    }

    @Override
    public void createFeed(long userId, FeedTypeEnum eventType, FeedOperationEnum eventOperation, long entityId) {
        Feed event = Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(eventOperation)
                .entityId(entityId)
                .build();
        feedRepository.updateFeed(event);
    }
}
