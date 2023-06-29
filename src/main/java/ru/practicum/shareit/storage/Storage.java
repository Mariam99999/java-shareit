package ru.practicum.shareit.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface Storage {
    User addUser(User user);

    User getUser(int id);

    void deleteUser(int id);

    List<User> getUsers();

    Item getItemById(int id);

    Item addItem(Item item);
    Item updateItem(Item item);

    List<Item> getOwnerItems(int ownerId);
    List<Item> getItems();

}
