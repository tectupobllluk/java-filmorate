package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.EventOperationEnum;
import ru.yandex.practicum.filmorate.enums.EventTypeEnum;

import javax.validation.ValidationException;
import java.time.Instant;


@Component
@RequiredArgsConstructor
public class DbFeedRepository implements FeedSaveDB {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveEvent(long userId, int eventType, int operationType, long entityId) {
        String sqlQuery = "INSERT INTO events (time_stamp, user_id, event_type, operation_type, entity_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, Instant.now().toEpochMilli(), userId, eventType, operationType, entityId);
    }

    @Override
    public int getEventTypeId(EventTypeEnum eventType) {
        switch (eventType) {
            case LIKE:
                return 1;
            case REVIEW:
                return 2;
            case FRIEND:
                return 3;
            default:
                throw new ValidationException("No such event type found.");
        }
    }

    @Override
    public int getOperationTypeId(EventOperationEnum operationType) {
        switch (operationType) {
            case REMOVE:
                return 1;
            case ADD:
                return 2;
            case UPDATE:
                return 3;
            default:
                throw new ValidationException("No such operation type found.");
        }
    }
}
