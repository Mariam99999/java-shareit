package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceUTest {
    @MockBean
    UserRepository userRepository;
    @Autowired
    UserDtoMapper userDtoMapper;
    @Autowired
    UserService userService;

    User user;
    User user2;
    UserDto userDto;

    @BeforeEach
    void init() {
        user = new User(1L, "tUserName", "mail@mail.ru");
        user2 = new User(2L, "tUserName2", "mail2@mail.ru");
        userDto = userDtoMapper.mapToDto(user);
        when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user));
    }

    @Test
    void addUser() {
        when(userRepository.save(any()))
                .thenReturn(user);
        assertEquals(user.getName(),
                userService.addUser(userDto).getName());
    }

    @Test
    void getUserById() {
        assertEquals(user.getName(), userService.getUserById(user.getId()).getName());
    }

    @Test
    void update() {
        userDto.setName("newName");
        assertEquals(userDto.getName(), userService.update(1L, userDto).getName());
    }

    @Test
    void deleteUser() {
        when(userRepository.existsById(any()))
                .thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository,
                Mockito.times(1))
                .deleteById(anyLong());
    }

    @Test
    void getUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));
        assertEquals(userDto, userService.getUsers().get(0));
    }
}
