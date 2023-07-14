package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.annotation.BookingDate;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Booking {
    private long id;
    @BookingDate
    private LocalDate start;
    @BookingDate
    private LocalDate end;
    @NonNull
    private Item item;
    @NonNull
    private User booker;
    private Status status;
}
