package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DbReviewLikesRepository implements ReviewLikesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer getUsefulByReviewId(long reviewId) {
        return jdbcTemplate.queryForObject("SELECT COALESCE(SUM(useful), 0) " +
                        "FROM useful_review " +
                        "WHERE review_id = ?;",
                Integer.class,
                reviewId);
    }

    @Override
    public void likeReview(long reviewId, long userId) {
        String checkExistenceQuery = "SELECT COUNT(*) FROM useful_review WHERE review_id = ? AND user_id = ?;";
        int count = jdbcTemplate.queryForObject(checkExistenceQuery, Integer.class, reviewId, userId);

        if (count > 0) {
            String updateQuery = "UPDATE useful_review SET useful = 1 WHERE review_id = ? AND user_id = ?;";
            jdbcTemplate.update(updateQuery, reviewId, userId);
        } else {
            String insertQuery = "INSERT INTO useful_review (review_id, user_id, useful) VALUES (?, ?, ?);";
            jdbcTemplate.update(insertQuery, reviewId, userId, 1);
        }
    }

    @Override
    public void dislikeReview(long reviewId, long userId) {
        String checkExistenceQuery = "SELECT COUNT(*) FROM useful_review WHERE review_id = ? AND user_id = ?;";
        int count = jdbcTemplate.queryForObject(checkExistenceQuery, Integer.class, reviewId, userId);

        if (count > 0) {
            String updateQuery = "UPDATE useful_review " +
                    "SET useful = - 1 " +
                    "WHERE review_id = ? " +
                    "AND user_id = ?;";
            jdbcTemplate.update(updateQuery, reviewId, userId);
        } else {
            String insertQuery = "INSERT INTO useful_review (review_id, user_id, useful) VALUES (?, ?, ?);";
            jdbcTemplate.update(insertQuery, reviewId, userId, -1);
        }
    }

    @Override
    public void removeLikeFromReview(long reviewId, long userId) {
        String sqlQuery = "DELETE FROM useful_review " +
                "WHERE review_id = ? " +
                "AND user_id = ? " +
                "AND useful = 1;";

        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    @Override
    public void removeDislikeFromReview(long reviewId, long userId) {
        String sqlQuery = "DELETE FROM useful_review " +
                "WHERE review_id = ? " +
                "AND user_id = ? " +
                "AND useful = -1;";

        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }
}
