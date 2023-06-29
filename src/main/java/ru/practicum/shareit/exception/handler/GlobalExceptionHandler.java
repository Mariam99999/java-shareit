package ru.practicum.shareit.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.CustomValidationException;
import ru.practicum.shareit.exception.NotUniqueEmail;
import ru.practicum.shareit.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResourceNotFoundException handleResourceNotFoundException(ResourceNotFoundException exception) {
        return exception;
    }

    @ExceptionHandler(NotUniqueEmail.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public NotUniqueEmail handleNotUniqueEmail(NotUniqueEmail exception) {
        return exception;
    }

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomValidationException handleCustomValidationException(CustomValidationException exception) {
        return exception;
    }


}
