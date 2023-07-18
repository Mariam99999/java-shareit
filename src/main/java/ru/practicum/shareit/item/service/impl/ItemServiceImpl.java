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
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemDtoMapper itemDtoMapper;

    @Override
    public ItemDto getItemById(long id) {
        Optional<ItemDto> item = itemRepository.findByIdDto(id);
        if (item.isEmpty()) throw new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage());
//        return itemDtoMapper.mapToDto(item.get());
        return item;
    }

    @Override
    public ItemDto addItem(long ownerId, ItemDto itemDto) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isEmpty())
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        return itemDtoMapper.mapToDto(itemRepository.save(itemDtoMapper.mapFromDto(itemDto, owner.get())));
    }

    @Override
    public List<ItemDto> getOwnerItems(long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream().map(itemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(long id, Long ownerId, ItemDto itemDto) {
        if (ownerId == null) throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        if (itemRepository.findById(id).isEmpty() || !itemRepository.findByOwnerId(ownerId).contains(itemRepository.findById(id).get()))
            throw new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage());
        Item item = itemRepository.findById(id).get();
        item.setName(!StringUtils.hasText(itemDto.getName()) ? item.getName() : itemDto.getName());
        item.setDescription(!StringUtils.hasText(itemDto.getDescription()) ? item.getDescription() : itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable() == null ? item.getAvailable() : itemDto.getAvailable());
        return itemDtoMapper.mapToDto(itemRepository.save(item));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) return List.of();
        return itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true)
                .stream()
                .map(itemDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
