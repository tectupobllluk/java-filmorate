package ru.yandex.practicum.filmorate.service;

public interface ReviewLikeService {

    void removeReviewUsefulness(long reviewId, long userId, int usefulness);

    void setReviewUsefulness(long reviewId, long userId, int usefulness);
}