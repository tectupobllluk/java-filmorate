package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DbReviewRepository implements ReviewRepository {

    private final ReviewLikesRepository reviewLikesRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Review> createReview(Review review) {
        String sqlQuery =
                "INSERT INTO reviews (content_review, is_positive, film_id, user_id) " +
                        "VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement stmt = connection.prepareStatement(
                            sqlQuery,
                            new String[]{"review_id"}
                    );
                    stmt.setString(1, review.getContent());
                    stmt.setBoolean(2, review.getIsPositive());
                    stmt.setLong(3, review.getFilmId());
                    stmt.setLong(4, review.getUserId());
                    return stmt;
                },
                keyHolder
        );
        int reviewId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        review.setReviewId(reviewId);
        return getReviewById(keyHolder.getKey().intValue());
    }

    @Override
    public Review updateReview(Review updatedReview) {
        String sql = "UPDATE reviews SET " +
                "content_review = ?, is_positive = ? " +
                "WHERE review_id = ?;";

        jdbcTemplate.update(sql,
                updatedReview.getContent(),
                updatedReview.getIsPositive(),
                updatedReview.getReviewId());
        return getReviewById(updatedReview.getReviewId()).orElse(null);
    }

    @Override
    public void deleteReview(long reviewId) {
        final String sql = "DELETE FROM reviews WHERE review_id = ?;";
        Optional<Review> optionalReview = getReviewById(reviewId);
        if (optionalReview.isPresent()) {
            jdbcTemplate.update(sql, reviewId);
        } else {
            throw new IllegalArgumentException("Review not found with id: " + reviewId);
        }
    }

    @Override
    public Optional<Review> getReviewById(long reviewId) {
        final String sql = "SELECT * " +
                "FROM reviews " +
                "WHERE review_id = ?;";
        final List<Review> reviews = jdbcTemplate.query(sql, new ReviewRowMapper(), reviewId);
        if (reviews.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(reviews.get(0));
    }

    @Override
    public List<Review> getReviewsByFilmId(long count) {
        final String sql = "SELECT r.* " +
                "FROM reviews r " +
                "ORDER BY (" +
                "  SELECT COALESCE(SUM(useful), 0) " +
                "  FROM useful_review u " +
                "  WHERE u.review_id = r.review_id" +
                ") DESC " +
                "LIMIT ?;";

        return jdbcTemplate.query(sql, new ReviewRowMapper(), count);
    }

    @Override
    public List<Review> getReviewsByFilmId(Long filmId, long count) {
        final String sql = "SELECT r.* " +
                "FROM reviews r " +
                "WHERE r.film_id = ? " +
                "ORDER BY (" +
                "  SELECT COALESCE(SUM(useful), 0) " +
                "  FROM useful_review u " +
                "  WHERE u.review_id = r.review_id" +
                ") DESC " +
                "LIMIT ?;";

        return jdbcTemplate.query(sql, new ReviewRowMapper(), filmId, count);
    }

    private class ReviewRowMapper implements RowMapper<Review> {
        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Review.builder()
                    .reviewId(rs.getLong("review_id"))
                    .content(rs.getString("content_review"))
                    .isPositive(rs.getBoolean("is_positive"))
                    .filmId(rs.getLong("film_id"))
                    .userId(rs.getLong("user_id"))
                    .useful(reviewLikesRepository.getUsefulByReviewId(rs.getInt("review_id")))
                    .build();
        }
    }
}
