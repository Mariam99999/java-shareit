package ru.practicum.shareit.item.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoWithBookerId;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public class ItemDtoMapper {

    public ItemDto mapToDto(Item model, BookingDtoWithBookerId lastBooking, BookingDtoWithBookerId nextBooking, List<CommentDto> comments) {
        return new ItemDto(model.getId(), model.getName(), model.getDescription(), model.getAvailable(), lastBooking, nextBooking,comments);
    }

    public Item mapFromDto(ItemDto model, User owner) {
        return new Item(model.getId(), model.getName(), model.getDescription(), model.getAvailable(), owner);
    }
}

