package ru.practicum.shareit.request.service.impi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoGet;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithListItem;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemRequestServiceImplUTest {
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRequestRepository itemRequestRepository;
    @Autowired
    ItemRequestMapper itemRequestMapper;
    @Autowired
    ItemDtoMapper itemDtoMapper;
    @Autowired
    ItemRequestService itemRequestService;
    User user;
    User user2;
    Item item;
    Item item2;
    LocalDateTime start;
    LocalDateTime end;
    Booking booking;
    BookingDto bookingDto;
    ItemRequest itemRequest;
    ItemRequestDto itemRequestDto;

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
        itemRequest = new ItemRequest(1L, "desc", user, start);
        itemRequestDto = new ItemRequestDto("desc");
        item.setRequest(itemRequest);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepository.findByRequestIdIn(any()))
                .thenReturn(List.of(item));
    }

    @Test
    void addRequest() {
        when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);
        ItemRequestDtoGet expectedItemRequestDtoGet = itemRequestService.addRequest(1L,
                itemRequestDto);
        assertEquals(itemRequest.getDescription(), expectedItemRequestDtoGet.getDescription());
    }

    @Test
    void getRequestsByRequestorId() {
        when(itemRequestRepository.findByRequestorId(anyLong(), any()))
                .thenReturn(List.of(itemRequest));
        List<ItemRequestDtoWithListItem> requestDtoWithListItems = itemRequestService.getRequestsByRequestorId(1L);
        assertFalse(requestDtoWithListItems.isEmpty());
        assertEquals(itemRequest.getDescription(), requestDtoWithListItems.get(0).getDescription());
    }

    @Test
    void getAllRequests() {
        when(itemRequestRepository.findAllByRequestorIdNot(anyLong(), any()))
                .thenReturn(List.of(itemRequest));
        List<ItemRequestDtoWithListItem> requestDtoWithListItems = itemRequestService.getAllRequests(1L, 1, 1);
        assertFalse(requestDtoWithListItems.isEmpty());
        assertEquals(itemRequest.getDescription(), requestDtoWithListItems.get(0).getDescription());

    }

    @Test
    void getRequestById() {
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(itemRequest));
        ItemRequestDtoWithListItem itemRequestDtoWithListItem = itemRequestService.getRequestById(1L, 1L);
        assertEquals(itemRequest.getDescription(), itemRequestDtoWithListItem.getDescription());
    }

}
