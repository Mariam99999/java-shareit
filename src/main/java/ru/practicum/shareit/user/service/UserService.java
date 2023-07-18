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
import ru.practicum.shareit.user.storage.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;


    public UserDto addUser(UserDto userDto) {
//        if (checkEmailExist(userDto.getEmail())) throw new NotUniqueEmail(Messages.NOT_UNIQUE_EMAIL.getMessage());
        try {
            User user = userRepository.save(userDtoMapper.mapFromDto(userDto));
            userDto.setId(user.getId());
            return userDto;
        } catch (ConstraintViolationException e) {
            throw new NotUniqueEmail(Messages.NOT_UNIQUE_EMAIL.getMessage());
        }
    }

    public UserDto getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        return userDtoMapper.mapToDto(user.get());
    }

    public UserDto update(long id, UserDto userDto) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        User user = userOptional.get();
        if (StringUtils.hasText(userDto.getName())) user.setName(userDto.getName());
        if (StringUtils.hasText(userDto.getEmail())) {
            if (!user.getEmail().equals(userDto.getEmail()) && checkEmailExist(userDto.getEmail()))
                throw new NotUniqueEmail(Messages.NOT_UNIQUE_EMAIL.getMessage());
            user.setEmail(userDto.getEmail());
        }
        return userDtoMapper.mapToDto(userRepository.save(user));
    }

    public void deleteUser(long id) {
        if (userRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        userRepository.deleteById(id);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(userDtoMapper::mapToDto).collect(Collectors.toList());
    }

    private boolean checkEmailExist(String email) {
        return getUsers().stream().anyMatch((u) -> u.getEmail().equals(email));
    }
}
