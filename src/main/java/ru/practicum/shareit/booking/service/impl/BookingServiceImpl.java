package ru.practicum.shareit.booking.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidArguments;
import ru.practicum.shareit.exception.Messages;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.booking.enums.State.CURRENT;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public Booking addBooking(long bookerId, BookingDto bookingDto) {
        if (!bookingDto.getEnd().isAfter(bookingDto.getStart()))
            throw new InvalidArguments(Messages.WRONG_DATE.getMessage());
        Item item = (Item) findByIdOrThrowError(bookingDto.getItemId(), itemRepository);
        if (!item.getAvailable()) throw new InvalidArguments(Messages.INVALID_ARGUMENTS.getMessage());
        User user = (User) findByIdOrThrowError(bookerId, userRepository);

        return bookingRepository.save(bookingMapper.mapFromDto(user, item, bookingDto));
    }

    @Override
    public Booking updateStatus(long userId, long bookingId, boolean approved) {
        Booking booking = (Booking) findByIdOrThrowError(bookingId, bookingRepository);
        if (booking.getItem().getOwner().getId() != userId)
            throw new InvalidArguments(Messages.NOT_ITEM_OWNER.getMessage());
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        Booking booking = (Booking) findByIdOrThrowError(bookingId, bookingRepository);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId)
            throw new InvalidArguments(Messages.INVALID_ARGUMENTS.getMessage());
        return booking;
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId, String stringState) {
        findByIdOrThrowError(userId,userRepository);
        List<Booking> bookings;
        State state;
        try{
            state = State.valueOf(stringState);
        } catch (Exception e){
            throw new InvalidArguments("Unknown state: " + stringState);
        }
        Sort sort = Sort.by("start").descending();
        LocalDateTime dateTime = LocalDateTime.now();
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartBeforeOrStartAndEndAfter(userId, dateTime,dateTime, dateTime, sort);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndBefore(userId,dateTime,sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartAfter(userId,dateTime,sort);
                break;
            case CANCELED:
                bookings = bookingRepository.findByBookerAndStatus(userId,Status.CANCELED,sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerAndStatus(userId,Status.REJECTED,sort);
                break;
            default:
                bookings = bookingRepository.findAllByBookerId(userId, sort);
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByItemOwner(Long userId, String state) {
        return bookingRepository.findBookingByItemOwner(userId);
    }

    private Object findByIdOrThrowError(Long id, JpaRepository repository) {
        Optional<Object> o = repository.findById(id);
        if (o.isEmpty()) throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage());
        return o.get();
    }


}
