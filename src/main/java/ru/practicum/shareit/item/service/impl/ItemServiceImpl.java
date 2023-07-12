package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.Messages;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final ItemDtoMapper itemDtoMapper;
    private int id = 0;

    @Override
    public ItemDto getItemById(int id) {
        Item item = itemStorage.getItemById(id);
        if (item == null) throw new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage());
        return itemDtoMapper.mapToDto(item);
    }

    @Override
    public ItemDto addItem(int ownerId, ItemDto itemDto) {
        if (userStorage.getUser(ownerId) == null)
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        int itemId = ++id;
        itemStorage.addItem(itemDtoMapper.mapFromDto(itemDto, itemId, ownerId));
        itemDto.setId(itemId);
        return itemDto;
    }

    @Override
    public List<ItemDto> getOwnerItems(int ownerId) {

        return itemStorage.getOwnerItems(ownerId).stream().map(itemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(int id, Integer ownerId, ItemDto itemDto) {
        if (ownerId == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        if (itemStorage.getOwnerItems(id) == null || !itemStorage.getOwnerItems(ownerId).contains(itemStorage.getItemById(id)))
            throw new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage());
        Item item = itemStorage.getItemById(id);
        item.setName(!StringUtils.hasText(itemDto.getName()) ? item.getName() : itemDto.getName());
        item.setDescription(!StringUtils.hasText(itemDto.getDescription()) ? item.getDescription() : itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable() == null ? item.getAvailable() : itemDto.getAvailable());
        return itemDtoMapper.mapToDto(item);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) return List.of();
        String textLowerCase = text.toLowerCase();
        return itemStorage.getItems().stream()
                .filter((i) -> i.getAvailable() && (i.getName().toLowerCase().contains(textLowerCase)
                        || i.getDescription().toLowerCase().contains(textLowerCase)))
                .map(itemDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
