package ru.practicum.shareit.exception;

public class CustomValidationException extends RuntimeException{
    public CustomValidationException() {
    }

    public CustomValidationException(String message) {
        super(message);
    }
}
