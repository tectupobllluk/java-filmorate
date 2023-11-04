package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private long reviewId;
    @NotBlank(message = "Не указан content")
    private String content;
    @NotNull(message = "Не указан isPositive")
    private Boolean isPositive;
    @NotNull(message = "Не указан id пользователя")
    private Long userId;
    @NotNull(message = "Не указан id фильма")
    private Long filmId;
    private int useful = 0;
}

