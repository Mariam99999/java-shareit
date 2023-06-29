package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.annotation.BookingDate;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Booking {
    private int id;
    @BookingDate
    private LocalDate start;
    @BookingDate
    private LocalDate end;
    @NonNull
    private Integer itemId;
    @NonNull
    private Integer bookerId;
    private Status status;
}
