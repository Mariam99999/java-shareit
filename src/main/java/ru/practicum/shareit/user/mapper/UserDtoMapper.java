package ru.practicum.shareit.user.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.DtoMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
@Component

public class UserDtoMapper implements DtoMapper<UserDto, User> {

    @Override
    public UserDto mapToDto(User model) {
        return  new UserDto(model.getName(), model.getEmail());
    }

    @Override
    public User mapFromDto(UserDto model, int id, Integer ownerId) {
        return new User(id,model.getName(),model.getEmail());
    }
}

