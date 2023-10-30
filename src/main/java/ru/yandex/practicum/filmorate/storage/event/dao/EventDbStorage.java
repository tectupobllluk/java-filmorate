package ru.yandex.practicum.filmorate.storage.event.dao;

 import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
 import ru.yandex.practicum.filmorate.model.feed.Event;
 import ru.yandex.practicum.filmorate.model.feed.EventOperationEnum;
 import ru.yandex.practicum.filmorate.model.feed.EventTypeEnum;
 import ru.yandex.practicum.filmorate.storage.event.EventStorageInterface;

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventStorageInterface {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getFeed(long userId) {
        final String sql = "SELECT event_id, timestamp, user_id, event_type, operation, entity_id " +
                "FROM feed " +
                "WHERE user_id = ? ";

        return jdbcTemplate.query(sql, this::mapRow, userId);
    }



    @Override
    public void createEvent(Event event) {
        final String sql = "INSERT INTO feed " +
                "(timestamp, user_id, event_type, operation, entity_id) values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().name(),
                event.getOperation().name(),
                event.getEntityId()
        );
    }

    private Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("event_id"))
                .timestamp(rs.getLong("timestamp"))
                .userId(rs.getInt("user_id"))
                .eventType(EventTypeEnum.valueOf(rs.getString("event_type")))
                .operation(EventOperationEnum.valueOf(rs.getString("operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
