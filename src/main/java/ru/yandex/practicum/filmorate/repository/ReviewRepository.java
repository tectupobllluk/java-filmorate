package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Optional<Review> createReview(Review review);

    Review updateReview(Review updatedReview);

    void deleteReview(long reviewId);

    Optional<Review> getReviewById(long reviewId);

    List<Review> getReviewsByFilmId(long count);

    List<Review> getReviewsByFilmId(Long filmId, long count);
}
