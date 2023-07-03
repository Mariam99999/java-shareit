package ru.practicum.shareit.item.storage.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, List<Integer>> ownerItems = new HashMap<>();

    @Override
    public Item getItemById(int id) {
        return items.get(id);
    }

    @Override
    public Item addItem(Item item) {
        int ownerId = item.getOwnerId();
        int itemId = item.getId();
        List<Integer> itemsId = ownerItems.get(ownerId);
        if (itemsId == null) itemsId = new ArrayList<>();
        itemsId.add(itemId);
        ownerItems.put(ownerId, itemsId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getOwnerItems(int ownerId) {
        List<Item> i = new ArrayList<>();
        List<Integer> itemsId = ownerItems.get(ownerId);
        if (itemsId != null) {
            for (int id : itemsId) {
                i.add(items.get(id));
            }
        }
        return i;
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }
}
