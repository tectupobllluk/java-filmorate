package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Event> getFeed(long userId) {
        final String sqlQuery = "SELECT e.*, " +
                "et.type_name as type_name, " +
                "ot.operation_name as operation_name " +
                "FROM events as e " +
                "JOIN event_types as et ON e. event_type = et.type_id " +
                "JOIN operation_types as ot ON e.operation_type = ot. operation_id " +
                "WHERE e.user_id = ?";


        return jdbcTemplate.query(sqlQuery, this::makeEvent, userId);
    }

    private Event makeEvent(ResultSet resultSet, long i) throws SQLException {
        return new Event(
                resultSet.getInt("event_id"),
                resultSet.getLong("time_stamp"),
                resultSet.getInt("user_id"),
                resultSet.getString("type_name"),
                resultSet.getString("operation_name"),
                resultSet.getInt("entity_id")
        );
    }
}
