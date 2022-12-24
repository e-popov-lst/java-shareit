package ru.practicum.shareit.common;

public class CheckParamException extends RuntimeException {
    public CheckParamException(final String message) {
        super(message);
    }
}