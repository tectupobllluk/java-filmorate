package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.repository.FeedRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseFeedService implements FeedService {

    private final FeedRepository feedRepository;
    private final UserService userService;

    public List<Feed> getFeedList(int id) {
        userService.getUser(id);
        return feedRepository.getFeedList(id);
    }
}
