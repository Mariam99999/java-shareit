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
        return storage.addItem(item);
    }
    public List<Item> searchItems(String text){
       return storage .getItems().stream()
                .filter((i) -> i.getName().contains(text) || i.getDescription().contains(text))
                .collect(Collectors.toList());
    }
}
