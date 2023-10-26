package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.repository.ReviewLikesRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@Service
public class BaseReviewLikeService implements ReviewLikeService {
    ReviewLikesRepository reviewLikesRepository;

    ReviewRepository reviewRepository;
    UserRepository userRepository;

    @Autowired
    public BaseReviewLikeService(ReviewLikesRepository reviewLikesRepository, ReviewRepository reviewRepository,
                                 UserRepository userRepository) {
        this.reviewLikesRepository = reviewLikesRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

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
