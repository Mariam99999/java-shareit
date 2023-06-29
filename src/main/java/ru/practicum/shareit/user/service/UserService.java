package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.Messages;
import ru.practicum.shareit.exception.NotUniqueEmail;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.storage.Storage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserService {
    private int id = 0;
    private final Storage storage;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public UserService(Storage storage, UserDtoMapper userDtoMapper) {
        this.storage = storage;
        this.userDtoMapper = userDtoMapper;
    }

    public User addUser(UserDto user) {
        if (checkEmailExist(user.getEmail()))
            throw new NotUniqueEmail(Messages.NOT_UNIQUE_EMAIL.getMessage());
        return storage.addUser(userDtoMapper.mapFromDto(user, ++id, null));
    }

    public User getUserById(int id) {
        User user = storage.getUser(id);
        if (user == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        return user;

    }

    public User update(int id, UserDto userDto) {
        if (storage.getUser(id) == null)
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        User user = storage.getUser(id);
        if (userDto.getName() == null) userDto.setName(user.getName());
        if (userDto.getEmail() == null) {
            userDto.setEmail(user.getEmail());
        } else if (!user.getEmail().equals(userDto.getEmail()) && checkEmailExist(userDto.getEmail()))
            throw new NotUniqueEmail(Messages.NOT_UNIQUE_EMAIL.getMessage());
        return storage.addUser(userDtoMapper.mapFromDto(userDto, id, null));
    }

    public void deleteUser(int id) {
        if (storage.getUser(id) == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        storage.deleteUser(id);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    private boolean checkEmailExist(String email) {
        return getUsers().stream().anyMatch((u) -> u.getEmail().equals(email));
    }
}
