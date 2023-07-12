package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(int id);

    ItemDto addItem(int ownerId, ItemDto itemDto);

    List<ItemDto> getOwnerItems(int ownerId);

    ItemDto updateItem(int id, Integer ownerId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);
}
