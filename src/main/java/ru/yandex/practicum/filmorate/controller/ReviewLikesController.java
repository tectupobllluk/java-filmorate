package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ReviewLikeService;
import ru.yandex.practicum.filmorate.service.ReviewService;


@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewLikesController {

    private final ReviewLikeService reviewLikeService;
    private final ReviewService reviewService;

    @PutMapping("/{id}/like/{userId}")
    public void likeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Put /{}/like/{} - Started!", id, userId);

        reviewLikeService.likeReview(id, userId);
        log.info("After like add - {}", reviewService.getReviewByFilmId(id, 10));
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Put /{}/dislike/{} - Started!", id, userId);

        reviewLikeService.dislikeReview(id, userId);
        log.info("After dislike add - {}", reviewService.getReviewByFilmId(id, 10));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFromReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete /{}/like/{} - Started!", id, userId);

        reviewLikeService.removeLikeFromReview(id, userId);
        log.info("After like remove - {}", reviewService.getReviewByFilmId(id, 10));
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislikeFromReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete /{}/dislike/{} - Started!", id, userId);

        reviewLikeService.removeDislikeFromReview(id, userId);
        log.info("After dislike remove - {}", reviewService.getReviewByFilmId(id, 10));
    }
}
