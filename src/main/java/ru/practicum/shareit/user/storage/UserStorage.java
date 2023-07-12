package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User getUser(int id);

    void deleteUser(int id);

    List<User> getUsers();

}
