package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbReviewLikesRepository implements ReviewLikesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbReviewLikesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getUsefulByReviewId(long reviewId) {
        Integer likeCounter = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(useful), 0) "
                        + "  FROM useful_review WHERE review_id = ?",
                Integer.class,
                reviewId);

        return likeCounter;
    }

    @Override
    public void likeReview(long reviewId, long userId) {
        // Проверяем, существует ли запись с заданными reviewId и userId
        String checkExistenceQuery = "SELECT COUNT(*) FROM useful_review WHERE (review_id = ? AND user_id = ?) ";
        int count = jdbcTemplate.queryForObject(checkExistenceQuery, Integer.class, reviewId, userId);

        if (count > 0) {
            // Если запись существует, обновляем ее useful
            String updateQuery = "UPDATE useful_review SET useful = 1 WHERE (review_id = ? AND user_id = ?) ";
            jdbcTemplate.update(updateQuery, reviewId, userId);
        } else {
            // Если запись не существует, создаем новую запись с useful = 1
            String insertQuery = "INSERT INTO useful_review (review_id, user_id, useful) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, reviewId, userId, 1);
        }
    }

    @Override
    public void dislikeReview(long reviewId, long userId) {
        // Проверяем, существует ли запись с заданными reviewId и userId
        String checkExistenceQuery = "SELECT COUNT(*) FROM useful_review WHERE (review_id = ? AND user_id = ?)";
        int count = jdbcTemplate.queryForObject(checkExistenceQuery, Integer.class, reviewId, userId);

        if (count > 0) {
            // Если запись существует, обновляем ее useful
            String updateQuery = "UPDATE useful_review SET useful = -1 WHERE (review_id = ? AND user_id = ?)";
            jdbcTemplate.update(updateQuery, reviewId, userId);
        } else {
            // Если запись не существует, создаем новую запись с useful = -1
            String insertQuery = "INSERT INTO useful_review (review_id, user_id, useful) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, reviewId, userId, -1);
        }

    }

    @Override
    public void removeLikeFromReview(long reviewId, long userId) {
        String sqlQuery = "DELETE FROM useful_review WHERE (review_id = ? AND user_id = ?)";

        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    @Override
    public void removeDislikeFromReview(long reviewId, long userId) {
        String sqlQuery = "DELETE FROM useful_review WHERE (review_id = ? AND user_id = ?)";

        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }
}
