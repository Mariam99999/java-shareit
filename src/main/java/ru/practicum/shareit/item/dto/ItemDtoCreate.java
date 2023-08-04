package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Setter
public class ItemDtoCreate {
    @NotBlank
    @Size(max = 20)
    private String name;
    @NotBlank
    @Size(max = 500)
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
}
