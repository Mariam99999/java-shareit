package ru.practicum.shareit.booking.dto;

import lombok.NonNull;
import ru.practicum.shareit.booking.annotation.BookingDate;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDate;

public class BookingDto {
    private long id;
    @BookingDate
    private LocalDate start;
    @BookingDate
    private LocalDate end;
    @NonNull
    private Long itemId;
    @NonNull
    private Long bookerId;
    private Status status;
}
