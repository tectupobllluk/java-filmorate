package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.LoginConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @LoginConstraint
    private String login;
    private String name;
    @Past
    @NotNull
    private LocalDate birthday;
}
