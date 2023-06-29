package ru.practicum.shareit.item.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.DtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemDtoMapper implements DtoMapper<ItemDto, Item> {
    @Override
    public ItemDto mapToDto(Item model) {
        return new ItemDto(model.getName(), model.getDescription(), model.getAvailable(), model.getRequest());
    }

    @Override
    public Item mapFromDto(ItemDto model, int id, Integer ownerId) {
        return new Item(id, model.getName(), model.getDescription(), model.getAvailable(), ownerId, model.getRequest());
    }
}

