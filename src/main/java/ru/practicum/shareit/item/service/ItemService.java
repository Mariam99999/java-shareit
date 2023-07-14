package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(long id);

    ItemDto addItem(long ownerId, ItemDto itemDto);

    List<ItemDto> getOwnerItems(long ownerId);

    ItemDto updateItem(long id, Long ownerId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);
}
