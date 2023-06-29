package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.annotation.BookingDate;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
public class Booking {
    private int id;
    @BookingDate
    private LocalDate start;
    @BookingDate
    private LocalDate end;
    @NonNull
    private int itemId;
    @NonNull
    private int bookerId;
    private Status status;
}
