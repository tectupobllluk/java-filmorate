package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.EventOperationEnum;
import ru.yandex.practicum.filmorate.enums.EventTypeEnum;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class DbUserRepository implements UserRepository {

    private final JdbcOperations jdbcTemplate;

    private final FeedSaveDB feedSaveDB;

    @Override
    public void saveUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?);";

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void updateUser(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?;";
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        int rowsAffected = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        if (rowsAffected == 0) {
            throw new NotFoundException("Unknown user with " + user.getId() + " id!");
        }
    }

    @Override
    public List<User> getAllUsers() {
        final String sqlQuery = "SELECT * " +
                "FROM users;";
        return jdbcTemplate.query(sqlQuery, new UserRowMapper());
    }

    @Override
    public Optional<User> getUser(long userId) {
        final String sqlQuery = "SELECT * " +
                "FROM users " +
                "WHERE user_id = ?;";
        final List<User> users = jdbcTemplate.query(sqlQuery, new UserRowMapper(), userId);
        if (users.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public void deleteUser(long userId) {
        final String sqlQuery = "DELETE FROM users " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public void addFriend(User user, User friend) {
        final String sqlQuery = "INSERT INTO friends (user_id, friend_id) " +
                "VALUES (?, ?);";
         feedSaveDB.saveEvent(user.getId(), feedSaveDB.getEventTypeId(EventTypeEnum.FRIEND), feedSaveDB.getOperationTypeId(EventOperationEnum.ADD), friend.getId());
        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());

    }

    @Override
    public void deleteFriend(User user, User friend) {
        final String sqlQuery = "DELETE FROM friends " +
                "WHERE (user_id = ? AND friend_id = ?) " +
                "OR (user_id = ? AND friend_id = ?);";
        feedSaveDB.saveEvent(user.getId(), feedSaveDB.getEventTypeId(EventTypeEnum.FRIEND), feedSaveDB.getOperationTypeId(EventOperationEnum.REMOVE), friend.getId());
        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId(), friend.getId(), user.getId());

    }

    @Override
    public List<User> getCommonFriends(User user, User friend) {
        final String sqlQuery = "SELECT * " +
                "FROM users AS u, friends AS f, friends AS o " +
                "WHERE u.user_id = f.friend_id " +
                "AND u.user_id = o.friend_id " +
                "AND f.user_id = ? " +
                "AND o.user_id = ?;";
        return jdbcTemplate.query(sqlQuery, new UserRowMapper(), user.getId(), friend.getId());
    }

    @Override
    public List<User> getFriends(User user) {
        final String sqlQuery = "SELECT * " +
                "FROM users AS u, friends AS f " +
                "WHERE u.user_id = f.friend_id " +
                "AND f.user_id = ?;";
        return jdbcTemplate.query(sqlQuery, new UserRowMapper(), user.getId());
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("user_id"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getDate("birthday").toLocalDate()
            );
        }
    }
}
