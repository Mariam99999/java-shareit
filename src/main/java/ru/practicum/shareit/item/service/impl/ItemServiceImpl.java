package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.Messages;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemDtoMapper itemDtoMapper;

    @Override
    public ItemDto getItemById(long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) throw new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage());
        List<Booking> pastBookings = bookingRepository.findByItemIdAndEndBefore(id, LocalDateTime.now(), Sort.by("end").descending());
        List<Booking> futureBookings = bookingRepository.findByItemIdAndStartAfter(id, LocalDateTime.now(), Sort.by("start").ascending());
        return itemDtoMapper.mapToDto(item.get(), pastBookings.get(0), futureBookings.get(0));
    }

    @Override
    public ItemDto addItem(long ownerId, ItemDto itemDto) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isEmpty())
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        return itemDtoMapper.mapToDto(itemRepository.save(itemDtoMapper.mapFromDto(itemDto, owner.get())), null, null);
    }

    @Override
    public List<ItemDto> getOwnerItems(long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream().map(i -> {
            List<Booking> pastBookings = bookingRepository.findByItemIdAndEndBefore(i.getId(), LocalDateTime.now(), Sort.by("end").descending());
            List<Booking> futureBookings = bookingRepository.findByItemIdAndStartAfter(i.getId(), LocalDateTime.now(), Sort.by("start").ascending());
            return itemDtoMapper.mapToDto(i, pastBookings.get(0), futureBookings.get(0));
        }).collect(Collectors.toList());
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
        List<Booking> pastBookings = bookingRepository.findByItemIdAndEndBefore(id, LocalDateTime.now(), Sort.by("end").descending());
        List<Booking> futureBookings = bookingRepository.findByItemIdAndStartAfter(id, LocalDateTime.now(), Sort.by("start").ascending());
        return itemDtoMapper.mapToDto(itemRepository.save(item), pastBookings.get(0), futureBookings.get(0));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) return List.of();
        return itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true)
                .stream()
                .map(i -> {
                    List<Booking> pastBookings = bookingRepository.findByItemIdAndEndBefore(i.getId(), LocalDateTime.now(), Sort.by("end").descending());
                    List<Booking> futureBookings = bookingRepository.findByItemIdAndStartAfter(i.getId(), LocalDateTime.now(), Sort.by("start").ascending());
                    return itemDtoMapper.mapToDto(i, pastBookings.get(0), futureBookings.get(0));
                })
                .collect(Collectors.toList());
    }
    private Optional<Booking> getLastBooking(long itemId){
        List<Booking> pastBookings = bookingRepository.findByItemIdAndEndBefore(itemId, LocalDateTime.now(), Sort.by("end").descending());
        if(pastBookings.isEmpty()) return Optional.empty();
        return Optional.of(pastBookings.get(0));
    }
    private Optional<Booking> getLastBooking(long itemId){
        List<Booking> pastBookings = bookingRepository.findByItemIdAndEndBefore(itemId, LocalDateTime.now(), Sort.by("end").descending());
        if(pastBookings.isEmpty()) return Optional.empty();
        return Optional.of(pastBookings.get(0));
    }
}
