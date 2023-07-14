package ru.practicum.shareit.item.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemDtoMapper {

    public ItemDto mapToDto(Item model) {
        return new ItemDto(model.getId(), model.getName(), model.getDescription(), model.getAvailable());
    }

    public Item mapFromDto(ItemDto model, User owner) {
        return new Item(model.getId(), model.getName(), model.getDescription(), model.getAvailable(), owner);
    }
}

