package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.Messages;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final Storage storage;
    private final ItemDtoMapper itemDtoMapper;
    private int id = 0;

    public Item getItemById(int id) {
        Item item = storage.getItemById(id);
        if (item == null) throw new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage());
        return item;
    }

    public Item addItem(int ownerId, ItemDto itemDto) {
        if (storage.getUser(ownerId) == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());

        return storage.addItem(itemDtoMapper.mapFromDto(itemDto, ++id, ownerId));
    }

    public List<Item> getOwnerItems(int ownerId) {
        return storage.getOwnerItems(ownerId);
    }

    public Item updateItem(int id, Integer ownerId, ItemDto itemDto) {
        if (ownerId == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        if (storage.getOwnerItems(id) == null || !storage.getOwnerItems(ownerId).contains(storage.getItemById(id)))
            throw new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage());
        Item item = storage.getItemById(id);
        item.setName(itemDto.getName() == null ? item.getName() : itemDto.getName());
        item.setDescription(itemDto.getDescription() == null ? item.getDescription() : itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable() == null ? item.getAvailable() : itemDto.getAvailable());
        return storage.updateItem(item);
    }

    public List<Item> searchItems(String text) {
        if(text.isEmpty()) return List.of();
        return storage.getItems().stream()
                .filter((i) -> i.getAvailable() && (i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }
}
