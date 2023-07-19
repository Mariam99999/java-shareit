package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.dto.BookingDtoWithBookerId;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.Messages;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentService;
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
    private final BookingMapper bookingMapper;
    private final CommentService commentService;

    @Override
    public ItemDto getItemById(long id, long userId) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) throw new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage());
        Optional<BookingDtoWithBookerId> pastBooking = getLastBookingDtoWithBookerId(id, userId);
        Optional<BookingDtoWithBookerId> futureBooking = getNextBookingDtoWithBookerId(id, userId);
        List<CommentDto> comments = commentService.getCommentsByItemId(id);
        return itemDtoMapper.mapToDto(item.get(), pastBooking.orElse(null), futureBooking.orElse(null), comments);
    }

    @Override
    public ItemDto addItem(long ownerId, ItemDto itemDto) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isEmpty())
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        List<CommentDto> comments = commentService.getCommentsByItemId(itemDto.getId());
        return itemDtoMapper.mapToDto(itemRepository.save(itemDtoMapper.mapFromDto(itemDto, owner.get())),
                null, null, comments);
    }

    @Override
    public List<ItemDto> getOwnerItems(long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream().map(i -> {
            Optional<BookingDtoWithBookerId> pastBooking = getLastBookingDtoWithBookerId(i.getId(), ownerId);
            Optional<BookingDtoWithBookerId> futureBooking = getNextBookingDtoWithBookerId(i.getId(), ownerId);
            List<CommentDto> comments = commentService.getCommentsByItemId(i.getId());
            return itemDtoMapper.mapToDto(i, pastBooking.orElse(null), futureBooking.orElse(null), comments);
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
        Optional<BookingDtoWithBookerId> pastBooking = getLastBookingDtoWithBookerId(id, ownerId);
        Optional<BookingDtoWithBookerId> futureBooking = getNextBookingDtoWithBookerId(id, ownerId);
        List<CommentDto> comments = commentService.getCommentsByItemId(id);
        return itemDtoMapper.mapToDto(itemRepository.save(item), pastBooking.orElse(null),
                futureBooking.orElse(null), comments);
    }

    @Override
    public List<ItemDto> searchItems(String text, long userId) {
        if (text.isBlank()) return List.of();
        return itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true)
                .stream()
                .map(i -> {
                    Optional<BookingDtoWithBookerId> pastBooking = getLastBookingDtoWithBookerId(i.getId(), userId);
                    Optional<BookingDtoWithBookerId> futureBooking = getNextBookingDtoWithBookerId(i.getId(), userId);
                    List<CommentDto> comments = commentService.getCommentsByItemId(i.getId());
                    return itemDtoMapper.mapToDto(i, pastBooking.orElse(null), futureBooking.orElse(null), comments);
                })
                .collect(Collectors.toList());
    }

    private Optional<BookingDtoWithBookerId> getLastBookingDtoWithBookerId(long itemId, long userId) {
        List<Booking> pastBookings = bookingRepository.findByItemIdAndItemOwnerIdAndStatusAndStartBefore(itemId, userId,
                Status.APPROVED, LocalDateTime.now(), Sort.by("end").descending());
        if (pastBookings.isEmpty()) return Optional.empty();
        return Optional.of(bookingMapper.mapToBookingDtoWithBookerId(pastBookings.get(0)));
    }

    private Optional<BookingDtoWithBookerId> getNextBookingDtoWithBookerId(long itemId, long userId) {
        List<Booking> futureBookings = bookingRepository.findByItemIdAndItemOwnerIdAndStatusAndStartAfter(itemId, userId,
                Status.APPROVED, LocalDateTime.now(), Sort.by("start").ascending());
        if (futureBookings.isEmpty()) return Optional.empty();
        return Optional.of(bookingMapper.mapToBookingDtoWithBookerId(futureBookings.get(0)));
    }
}
