package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.FeedOperationEnum;
import ru.yandex.practicum.filmorate.enums.FeedTypeEnum;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseReviewService implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;

    @Override
    public Review createReview(Review review) {
        userRepository.getUser(review.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        filmRepository.getFilm(review.getFilmId())
                .orElseThrow(() -> new NotFoundException("Film not found"));

        Review reviewCreated = reviewRepository.createReview(review)
                .orElseThrow(() -> new NotFoundException("Failed to create a review"));
        feedService.createFeed(reviewCreated.getUserId(), FeedTypeEnum.REVIEW, FeedOperationEnum.ADD, reviewCreated.getReviewId());
        return reviewCreated;
    }

    @Override
    public Review updateReview(Review review) {
        userRepository.getUser(review.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        filmRepository.getFilm(review.getFilmId())
                .orElseThrow(() -> new NotFoundException("Film not found"));
        reviewRepository.getReviewById(review.getReviewId())
                .orElseThrow(() -> new NotFoundException("Failed to create a review"));
        Review reviewUpdated = reviewRepository.updateReview(review);
        feedService.createFeed(reviewUpdated.getUserId(), FeedTypeEnum.REVIEW, FeedOperationEnum.UPDATE, reviewUpdated.getReviewId());
        return reviewUpdated;
    }

    @Override
    public void deleteReview(long reviewId) {
        Review review = getReviewById(reviewId);
        reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));
        reviewRepository.deleteReview(reviewId);
        feedService.createFeed(review.getUserId(), FeedTypeEnum.REVIEW, FeedOperationEnum.REMOVE, review.getReviewId());
    }

    @Override
    public Review getReviewById(long reviewId) {
        return reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));
    }

    @Override
    public List<Review> getReviewByFilmId(Long filmId, long count) {
        if (filmId > 0) {
            filmRepository.getFilm(filmId)
                    .orElseThrow(() -> new NotFoundException("Film not found with id = " + filmId));
            return reviewRepository.getReviewsByFilmId(filmId, count);
        } else {
            return reviewRepository.getReviewsByFilmId(count);
        }
    }
}
