package ru.yandex.practicum.filmorate.repository;

public interface ReviewLikesRepository {

    Integer getUsefulByReviewId(long reviewId);

    void removeReviewUsefulness(long reviewId, long userId, int usefulness);

    void setReviewUsefulness(long reviewId, long userId, int usefulness);
}
