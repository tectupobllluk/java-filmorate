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

    public void setReviewUsefulness(long reviewId, long userId, int usefulness) {
//        String checkExistenceQuery = "SELECT COUNT(*) FROM useful_review WHERE review_id = ? AND user_id = ?;";
//        int count = jdbcTemplate.queryForObject(checkExistenceQuery, Integer.class, reviewId, userId);
//
//        if (count > 0) {
//            String updateQuery = "UPDATE useful_review " +
//                    "SET useful = ? " +
//                    "WHERE review_id = ? " +
//                    "AND user_id = ?;";
//            jdbcTemplate.update(updateQuery, usefulness, reviewId, userId);
//        } else {
        String sqlQuery = "merge into useful_review(review_id, user_id, useful) values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery, reviewId, userId, usefulness);
//        }
    }

    @Override
    public void removeReviewUsefulness(long reviewId, long userId, int usefulness) {
        String sqlQuery = "DELETE FROM useful_review " +
                "WHERE review_id = ? " +
                "AND user_id = ? " +
                "AND useful = ?;";

        jdbcTemplate.update(sqlQuery, reviewId, userId, usefulness);
    }
}
