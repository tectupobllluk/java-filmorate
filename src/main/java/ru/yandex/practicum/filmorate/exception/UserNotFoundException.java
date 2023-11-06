package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserNotFoundException extends RuntimeException {
    private final int id;

    public UserNotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }
}
