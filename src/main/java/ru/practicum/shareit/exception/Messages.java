package ru.practicum.shareit.exception;

import org.springframework.util.StringUtils;

public enum Messages {
    USER_NOT_FOUND,
    NOT_UNIQUE_EMAIL,
    ITEM_NOT_FOUND,
    VALIDATION_EXCEPTION;

    public String getMessage() {
        return StringUtils.capitalize(this.name().toLowerCase().replaceAll("_", " "));
    }
}
