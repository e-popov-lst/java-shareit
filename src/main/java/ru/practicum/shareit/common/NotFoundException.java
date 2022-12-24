package ru.practicum.shareit.common;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message, final String className) {
        super(className + " " + message);
    }
}