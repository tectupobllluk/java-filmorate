package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.ReleaseDateConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Film {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotEmpty
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    @NotNull
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
}
