package ru.yandex.practicum.filmorate.repository;

public interface ReviewLikesRepository {

    Integer getUsefulByReviewId(long reviewId);

    void likeReview(long reviewId, long userId);

    void dislikeReview(long reviewId, long userId);

    void removeLikeFromReview(long reviewId, long userId);

    void removeDislikeFromReview(long reviewId, long userId);
}
