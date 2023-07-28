package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class ItemRequestDtoGet {
    private Long id;
    private String description;
    private LocalDate created;
}
