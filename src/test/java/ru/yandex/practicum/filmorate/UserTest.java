package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validateCorrectUser() {
        User user = new User(1L, "test@test.ru", "test",
                "Name", LocalDate.of(2000, 2, 9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "Normal formatted user");
    }

    @Test
    void validateEmptyName() {
        User user = new User(1L, "test@test.ru", "test",
                "", LocalDate.of(2000, 2, 9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "Empty name");
    }

    @Test
    void validateEmptyEmail() {
        User user = new User(1L, "", "test",
                "Name", LocalDate.of(2000, 2, 9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Empty email");
    }

    @Test
    void validateWrongEmail() {
        User user = new User(1L, "email.ru", "test",
                "Name", LocalDate.of(2000, 2, 9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Wrong email");
    }

    @Test
    void validateEmptyLogin() {
        User user = new User(1L, "test@test.ru", "",
                "Name", LocalDate.of(2000, 2, 9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Empty login");
    }

    @Test
    void validateLoginWithSpaces() {
        User user = new User(1L, "test@test.ru", "test test",
                "Name", LocalDate.of(2000, 2, 9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Login with spaces");
    }

    @Test
    void validateBirthdayInFuture() {
        User user = new User(1L, "test@test.ru", "test",
                "Name", LocalDate.of(2030, 2, 9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Birthday in future");
    }
}
