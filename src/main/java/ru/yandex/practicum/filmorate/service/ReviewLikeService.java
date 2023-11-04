package ru.yandex.practicum.filmorate.service;

public interface ReviewLikeService {

    void likeReview(long reviewId, long userId);

    void dislikeReview(long reviewId, long userId);

    void removeLikeFromReview(long reviewId, long userId);

    void removeDislikeFromReview(long reviewId, long userId);
}