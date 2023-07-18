package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
@Component
public class BookingMapper {
    public BookingDto mapToDto(Booking booking){
        return new BookingDto(booking.getId(),booking.getStart(),booking.getEnd(),booking.getItem().getId());
    }
    public Booking mapFromDto(User booker, Item item, BookingDto bookingDto){
        return new Booking(bookingDto.getId(),bookingDto.getStart(),bookingDto.getEnd(), item, booker, Status.WAITING);
    }
}
