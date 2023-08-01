package ru.practicum.shareit.booking.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InvalidArguments;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@SpringBootTest
@DirtiesContext(classMode = AFTER_CLASS)
class BookingServiceImpUTest {

    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    UserRepository userRepository;
    @Autowired
    BookingServiceImpl bookingService;
    @Autowired
    BookingMapper bookingMapper;
    @Autowired
    ItemDtoMapper itemDtoMapper;
    @Autowired
    UserDtoMapper userDtoMapper;
    User user;
    User user2;
    Item item;
    Item item2;
    LocalDateTime start;
    LocalDateTime end;
    Booking booking;
    BookingDto bookingDto;

    @BeforeEach
    void init() {
        user = new User(1L, "tUserName", "mail@mail.ru");
        user2 = new User(2L, "tUserName2", "mail2@mail.ru");
        item = new Item(1L, "tName", "tDescription", true, user, null);
        item2 = new Item(2L, "tName2", "tDescription2", true, user2, null);
        item2 = new Item(2L, "tName2", "tDescription2", true, user2, null);
        start = LocalDateTime.now().plusMinutes(30);
        end = LocalDateTime.now().plusMinutes(90);
        booking = new Booking(1L, start, end, item2, user, Status.WAITING);
        bookingDto = new BookingDto(1L, start, end, item2.getId());
    }


    @Test
    void addBooking() {
        Mockito.
                when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item2));
        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.
                when(bookingRepository.save(Mockito.any()))
                .thenReturn(Optional.of(booking).get());

        BookingDtoGet expectedBookingDtoGet = bookingService.addBooking(user.getId()
                , bookingDto);
        assertEquals(expectedBookingDtoGet.getId(), bookingDto.getId());
        assertEquals(expectedBookingDtoGet.getItem().getId(), bookingDto.getItemId());
        assertEquals(expectedBookingDtoGet.getStart(), bookingDto.getStart());
        assertEquals(expectedBookingDtoGet.getEnd(), bookingDto.getEnd());

    }

    @Test
    void updateStatus() {

        Mockito.
                when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));

        BookingDtoGet expectedBookingDtoGet = bookingService.updateStatus(user2.getId(), booking.getId(), true);
        assertEquals(expectedBookingDtoGet.getId(), booking.getId());
        assertEquals(expectedBookingDtoGet.getStatus(), Status.APPROVED);

        BookingDtoGet expectedBookingDtoGet2 = bookingService.updateStatus(user2.getId(), booking.getId(), false);
        assertEquals(expectedBookingDtoGet2.getId(), booking.getId());
        assertEquals(expectedBookingDtoGet2.getStatus(), Status.REJECTED);

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.updateStatus(user.getId(),
                        booking.getId(), true));
        assertThrows(InvalidArguments.class, () ->
                bookingService.updateStatus(user2.getId(),
                        booking.getId(), false));
    }

    @Test
    void getBookingById() {
        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.
                when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));


        BookingDtoGet expectedBookingDtoGet = bookingService.getBookingById(user2.getId()
                , booking.getId());
        assertEquals(expectedBookingDtoGet.getId(), bookingDto.getId());
        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.getBookingById(10L, booking.getId()));

    }

    @Test
    void getBookings() {
        bookingMapper = Mockito.mock(BookingMapper.class);
        Mockito.when(bookingMapper.mapToBookingDtoGet(Mockito.any(), Mockito.any()
                        , Mockito.any()))
                .thenReturn(new BookingDtoGet(booking.getId(), start,
                        end, itemDtoMapper.mapToItemDtoGet(item),
                        userDtoMapper.mapToUserDtoGet(user), Status.WAITING));
        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        bookingService.getBookings(user.getId(), "CURRENT"
                , true, 2, 1);

        Mockito.verify(bookingRepository,
                        Mockito.times(1))
                .findByBookerIdAndStartBeforeAndEndAfter(Mockito.anyLong(),
                        Mockito.any(), Mockito.any(), Mockito.any());
        bookingService.getBookings(user.getId(), "PAST"
                , true, 2, 1);
        Mockito.verify(bookingRepository,
                        Mockito.times(1))
                .findByBookerIdAndEndBefore(Mockito.anyLong(),
                        Mockito.any(), Mockito.any());
      bookingService.getBookings(user.getId(), "FUTURE"
               , true, 2, 1);
        Mockito.verify(bookingRepository,
                        Mockito.times(1))
                .findByBookerIdAndStartAfter(Mockito.anyLong(),Mockito.any(),Mockito.any());
        bookingService.getBookings(user.getId(), "WAITING"
               , false, 2, 1);
        Mockito.verify(bookingRepository,
                        Mockito.times(1))
                .findAllByItemOwnerIdAndStatus(Mockito.anyLong(),
                        Mockito.any(), Mockito.any());

       bookingService.getBookings(user.getId(), "REJECTED"
                , true, 2, 1);
        Mockito.verify(bookingRepository,
                        Mockito.times(1))
                .findByBookerIdAndStatus(Mockito.anyLong(),
                        Mockito.any(), Mockito.any());
       bookingService.getBookings(user.getId(), "ALL"
                , true, 2, 1);
        Mockito.verify(bookingRepository,
                        Mockito.times(1))
                .findByBookerId(Mockito.anyLong(),
                        Mockito.any());
    }
}

