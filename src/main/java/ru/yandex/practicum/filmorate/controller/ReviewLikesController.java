package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.ReviewLikeService;


@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewLikesController {

    private final ReviewLikeService reviewLikeService;

    @Autowired
    public ReviewLikesController(ReviewLikeService reviewLikeService) {
        this.reviewLikeService = reviewLikeService;
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Put /{}/like/{} - Started!", id, userId);

        reviewLikeService.likeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Put /{}/dislike/{} - Started!", id, userId);

        reviewLikeService.dislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFromReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete /{}/like/{} - Started!", id, userId);

        reviewLikeService.removeLikeFromReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislikeFromReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete /{}/dislike/{} - Started!", id, userId);

        reviewLikeService.removeDislikeFromReview(id, userId);
    }
}
