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
    public void likeReview(long reviewId, long userId) {
        reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));

        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        reviewLikesRepository.likeReview(reviewId, userId);
    }

    @Override
    public void dislikeReview(long reviewId, long userId) {
        reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));

        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        reviewLikesRepository.dislikeReview(reviewId, userId);
    }

    @Override
    public void removeLikeFromReview(long reviewId, long userId) {
        reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));

        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        reviewLikesRepository.removeLikeFromReview(reviewId, userId);
    }

    @Override
    public void removeDislikeFromReview(long reviewId, long userId) {
        reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));

        userRepository.getUser(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        reviewLikesRepository.removeDislikeFromReview(reviewId, userId);
    }
}
