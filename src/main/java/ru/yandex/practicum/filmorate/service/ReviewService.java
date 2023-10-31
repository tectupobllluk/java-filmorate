package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(long reviewId);

    Review getReviewById(long reviewId);

    List<Review> getReviewByFilmId(Long filmId, long count);
}
