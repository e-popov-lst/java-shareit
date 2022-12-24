package ru.practicum.shareit.common;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
