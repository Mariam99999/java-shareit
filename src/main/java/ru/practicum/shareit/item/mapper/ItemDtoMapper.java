package ru.practicum.shareit.item.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoWithBookerId;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoGet;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public class ItemDtoMapper {

    public ItemDto mapToDto(Item model, List<BookingDtoWithBookerId> list, List<CommentDto> comments) {
        return new ItemDto(model.getId(), model.getName(), model.getDescription(), model.getAvailable(),
                list.get(0), list.get(1), comments);
    }

    public Item mapFromDtoCreate(ItemDtoCreate model, User owner) {
        return new Item(null, model.getName(), model.getDescription(), model.getAvailable(), owner);
    }

    public ItemDtoGet mapToItemDtoGet(Item item) {
        return new ItemDtoGet(item.getId(), item.getName());
    }
}

