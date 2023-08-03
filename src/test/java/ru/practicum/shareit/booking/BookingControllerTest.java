package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final BookingMapper bookingMapper = new BookingMapper();
    private final ItemDtoMapper itemDtoMapper = new ItemDtoMapper();
    private final UserDtoMapper userDtoMapper = new UserDtoMapper();
    private User user;
    private User user2;
    private User user3;
    private Item item;
    private Item item2;
    private LocalDateTime start;
    private LocalDateTime end;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingDtoGet bookingDtoGet;


    @BeforeEach
    void init() {
        user = new User(1L, "tUserName", "mail@mail.ru");
        user2 = new User(2L, "tUserName2", "mail2@mail.ru");
        user3 = new User(3L, "tUserName3", "mail3@mail.ru");
        item = new Item(1L, "tName", "tDescription", true, user, null);
        item2 = new Item(2L, "tName2", "tDescription2", true, user2, null);
        item2 = new Item(2L, "tName2", "tDescription2", true, user2, null);
        start = LocalDateTime.now().plusMinutes(30);
        end = LocalDateTime.now().plusMinutes(90);
        booking = new Booking(1L, start, end, item, user, Status.WAITING);
        bookingDto = bookingMapper.mapToDto(booking);
        bookingDtoGet = bookingMapper.mapToBookingDtoGet(booking, itemDtoMapper.mapToItemDtoGet(item),userDtoMapper.mapToUserDtoGet(user));
    }

    @Test
    void addBooking() throws Exception {

        when(bookingService.addBooking(anyLong(), any())).thenReturn(bookingDtoGet);
        mvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoGet.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDtoGet.getItem().getName())));

    }

    @Test
    void updateStatus() {

    }

    @Test
    void getBookingById() {
    }

    @Test
    void getBookingsByUserId() {
    }

    @Test
    void getBookingsByItemOwner() {
    }
}