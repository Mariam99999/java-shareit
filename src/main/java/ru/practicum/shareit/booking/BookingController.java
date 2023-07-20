package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking addBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId, @Validated @RequestBody BookingDto bookingDto) {
        return bookingService.addBooking(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.updateStatus(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state) {

        return bookingService.getBookings(userId, state, true);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsByItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookings(userId, state, false);
    }

}
