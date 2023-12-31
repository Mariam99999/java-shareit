package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.dto.ItemDtoGet;
import ru.practicum.shareit.user.dto.UserDtoGet;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoGet {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoGet item;
    private UserDtoGet booker;
    private Status status;
}
