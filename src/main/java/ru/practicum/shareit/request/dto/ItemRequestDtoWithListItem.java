package ru.practicum.shareit.request.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDtoWithListItem {
    private String description;
    private LocalDate created;
    private List<ItemDtoWithRequestId> items;
}
