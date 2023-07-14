package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User getUser(long id);

    void deleteUser(long id);

    List<User> getUsers();

}
