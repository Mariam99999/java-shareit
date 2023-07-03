package ru.practicum.shareit.user.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.Messages;
import ru.practicum.shareit.exception.NotUniqueEmail;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class UserService {
    private final UserStorage storage;
    private final UserDtoMapper userDtoMapper;
    private int id = 0;


    public UserDto addUser(UserDto userDto) {
        if (checkEmailExist(userDto.getEmail())) throw new NotUniqueEmail(Messages.NOT_UNIQUE_EMAIL.getMessage());
        int userId = ++id;
        storage.addUser(userDtoMapper.mapFromDto(userDto, userId, null));
        userDto.setId(userId);
        return userDto;
    }

    public UserDto getUserById(int id) {
        User user = storage.getUser(id);
        if (user == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        return userDtoMapper.mapToDto(user);
    }

    public UserDto update(int id, UserDto userDto) {
        User user = storage.getUser(id);
        if (user == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        if (StringUtils.hasText(userDto.getName())) user.setName(userDto.getName());
        if (StringUtils.hasText(userDto.getEmail())) {
            if (!user.getEmail().equals(userDto.getEmail()) && checkEmailExist(userDto.getEmail()))
                throw new NotUniqueEmail(Messages.NOT_UNIQUE_EMAIL.getMessage());
            user.setEmail(userDto.getEmail());
        }
        return userDtoMapper.mapToDto(user);
    }

    public void deleteUser(int id) {
        if (storage.getUser(id) == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        storage.deleteUser(id);
    }

    public List<UserDto> getUsers() {
        return storage.getUsers().stream().map(userDtoMapper::mapToDto).collect(Collectors.toList());
    }

    private boolean checkEmailExist(String email) {
        return getUsers().stream().anyMatch((u) -> u.getEmail().equals(email));
    }
}
