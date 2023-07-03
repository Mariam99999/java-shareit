package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item getItemById(int id);

    Item addItem(Item item);

    Item updateItem(Item item);

    List<Item> getOwnerItems(int ownerId);

    List<Item> getItems();
}
