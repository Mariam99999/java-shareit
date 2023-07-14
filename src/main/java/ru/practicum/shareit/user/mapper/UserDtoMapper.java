package ru.practicum.shareit.user.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component

public class UserDtoMapper {

    public UserDto mapToDto(User model) {
        return new UserDto(model.getId(), model.getName(), model.getEmail());
    }

    public User mapFromDto(UserDto model) {
        return new User(model.getId(), model.getName(), model.getEmail());
    }
}

