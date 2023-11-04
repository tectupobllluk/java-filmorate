package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public Review createReview(Review review) {
        userRepository.getUser(review.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        filmRepository.getFilm(review.getFilmId())
                .orElseThrow(() -> new NotFoundException("Film not found"));

        return reviewRepository.createReview(review)
                .orElseThrow(() -> new NotFoundException("Failed to create a review"));
    }

    @Override
    public Review updateReview(Review review) {
        userRepository.getUser(review.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        filmRepository.getFilm(review.getFilmId())
                .orElseThrow(() -> new NotFoundException("Film not found"));
        reviewRepository.getReviewById(review.getReviewId())
                .orElseThrow(() -> new NotFoundException("Failed to create a review"));

        return reviewRepository.updateReview(review);
    }

    @Override
    public void deleteReview(long reviewId) {
        reviewRepository.getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id = " + reviewId));

        reviewRepository.deleteReview(reviewId);
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
