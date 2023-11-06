package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.enums.FeedOperationEnum;
import ru.yandex.practicum.filmorate.enums.FeedTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbFeedRepository implements FeedRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Feed> getFeedList(long id) {
        String sqlQuery = "SELECT * FROM feeds WHERE user_id = ?";

        return jdbcTemplate.query(sqlQuery, this::rowMapperForFeed, id);

    }

    private Feed rowMapperForFeed(ResultSet rs, int rowNum) throws SQLException {
        return Feed.builder()
                .eventId(rs.getInt("event_id"))
                .timestamp(rs.getLong("timestamp"))
                .userId(rs.getInt("user_id"))
                .eventType(FeedTypeEnum.valueOf(rs.getString("event_type")))
                .operation(FeedOperationEnum.valueOf(rs.getString("operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }

    @Override
    public void updateFeed(Feed feed) {
        final String sql = "INSERT INTO feeds " +
                "(timestamp, user_id, event_type, operation, entity_id) values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                feed.getTimestamp(),
                feed.getUserId(),
                feed.getEventType().name(),
                feed.getOperation().name(),
                feed.getEntityId()
        );
    }
}