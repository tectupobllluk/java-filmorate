package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.repository.ReviewLikesRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BaseReviewLikeService implements ReviewLikeService {
    private final ReviewLikesRepository reviewLikesRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public void removeReviewUsefulness(long reviewId, long userId, int usefulness) {
        reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));

        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        reviewLikesRepository.removeReviewUsefulness(reviewId, userId, usefulness);
    }

    public void setReviewUsefulness(long reviewId, long userId, int usefulness) {
        reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));

        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        reviewLikesRepository.setReviewUsefulness(reviewId, userId, usefulness);
    }
}
