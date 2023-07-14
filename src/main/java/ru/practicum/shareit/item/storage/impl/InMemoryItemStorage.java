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
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Long>> ownerItems = new HashMap<>();

    @Override
    public Item getItemById(long id) {
        return items.get(id);
    }

    @Override
    public Item addItem(Item item) {
        long ownerId = item.getOwner().getId();
        long itemId = item.getId();
        List<Long> itemsId = ownerItems.get(ownerId);
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
    public List<Item> getOwnerItems(long ownerId) {
        List<Item> i = new ArrayList<>();
        List<Long> itemsId = ownerItems.get(ownerId);
        if (itemsId != null) {
            for (long id : itemsId) {
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
