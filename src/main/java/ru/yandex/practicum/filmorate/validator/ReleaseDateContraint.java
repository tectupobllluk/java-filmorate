package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReleaseDateContraint {
    String message() default "Film release date must be after 28 december 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
