package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequest {
    private int id;
    @NotBlank
    private String description;
    @NotNull
    private Integer requestorId;
    private LocalDate created;
}
