package ru.practicum.shareit.item.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class ItemServiceImplTestUT {
    @Autowired
    ItemServiceImpl itemService;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    BookingRepository bookingRepository;
    @Autowired
    ItemDtoMapper itemDtoMapper;
    @Autowired
    BookingMapper bookingMapper;
    @MockBean
    CommentRepository commentRepository;
    @Autowired
    CommentDtoMapper commentDtoMapper;
    @MockBean
    ItemRequestRepository itemRequestRepository;
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
    void getItemById() {
        Mockito.
                when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));
        Mockito.
                when(commentRepository.findByItemId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of());
        Mockito.
                when(bookingRepository.findByItemId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of());
        ItemDto expectedItemDto = itemService.getItemById(item.getId(), user.getId());
        assertEquals(expectedItemDto.getId(), item.getId());
        assertEquals(expectedItemDto.getName(), item.getName());
    }

    @Test
    void addItem() {
        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.
                when(itemRepository.save(Mockito.any()))
                .thenReturn(item);
        ItemDtoCreate itemDtoCreate = new ItemDtoCreate(item.getName(), item.getDescription(), item.getAvailable(), null);
        ItemDto expectedItemDto = itemService.addItem(user.getId(), itemDtoCreate);
        assertEquals(expectedItemDto.getId(), item.getId());
        assertEquals(expectedItemDto.getName(), item.getName());
    }

    @Test
    void getOwnerItems() {
        Mockito.
                when(commentRepository.findByItemOwnerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of());
        Mockito.
                when(bookingRepository.findByItemOwnerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of());
        Mockito.
                when(itemRepository.findByOwnerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(item));
        List<ItemDto> expectedItemDto = itemService.getOwnerItems(user.getId(), 1, 1);
        assertFalse(expectedItemDto.isEmpty());
        assertEquals(expectedItemDto.get(0).getId(), item.getId());

    }

    @Test
    void updateItem() {
        Mockito.
                when(commentRepository.findByItemOwnerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of());
        Mockito.
                when(bookingRepository.findByItemId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of());
        Mockito.
                when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));
        Mockito.
                when(itemRepository.save(Mockito.any()))
                .thenReturn(item);
        ItemDto itemDto = itemDtoMapper.mapToDto(item, null, null, List.of());
        itemDto.setName("newName");
        ItemDto expectedItemDto = itemService.updateItem(item.getId(), user.getId()
                , itemDto);
        assertEquals(expectedItemDto.getId(), item.getId());
        assertEquals(expectedItemDto.getName(), "newName");

    }

    @Test
    void searchItems() {
    }
}