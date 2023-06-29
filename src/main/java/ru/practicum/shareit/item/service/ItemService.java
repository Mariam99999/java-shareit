package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItemById(int id);

    Item addItem(int ownerId, ItemDto itemDto);

    List<Item> getOwnerItems(int ownerId);

    Item updateItem(int id, Integer ownerId, ItemDto itemDto);

    List<Item> searchItems(String text);
}
