package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {
    ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review createReview(@RequestBody @Valid Review review) {
        log.info("Create review: {} - Started!", review);
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody @Valid Review review) {
        log.info("Update review: {} - Started!", review);
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable long id) {
        log.debug("Delete request /reviews/{}", id);
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable long id) {
        log.debug("Get request /reviews/{}", id);
        return reviewService.getReviewById(id);
    }

    @GetMapping()
    public List<Review> getReviewByFilmId(
            @RequestParam(value = "filmId", defaultValue = "0") Long filmId,
            @RequestParam(value = "count", defaultValue = "10") long count
    ) {
        if (count <= 0) {
            throw new BadRequestException(
                    "If you don't want to specify the count, just don't specify it."
            );
        }
        return reviewService.getReviewByFilmId(filmId, count);
    }
}
