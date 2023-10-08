package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private final String error;

    public ErrorResponse(String message) {
        error = message;
    }

    public String getError() {
        return error;
    }
}
