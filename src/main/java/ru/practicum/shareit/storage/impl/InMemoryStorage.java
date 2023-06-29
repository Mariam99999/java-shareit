package ru.practicum.shareit.storage.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.Storage;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryStorage implements Storage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, List<Integer>> ownerItems = new HashMap<>();


    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);

    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

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
