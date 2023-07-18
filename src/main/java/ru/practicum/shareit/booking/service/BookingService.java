package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(long bookerId, BookingDto bookingDto);

    Booking updateStatus(long userId, long bookingId, boolean approved);

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getBookingsByUserId(Long userId, String state);

    List<Booking> getBookingsByItemOwner(Long userId, String state);
}
