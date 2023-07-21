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
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemDtoMapper itemDtoMapper;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentDtoMapper commentDtoMapper;


    @Override
    public ItemDto getItemById(long id, long userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Messages.ITEM_NOT_FOUND.getMessage()));
        List<CommentDto> comments = commentRepository.findByItemId(id, Sort.by("created")).stream().map(commentDtoMapper::mapToDto).collect(Collectors.toList());
        List<Optional<BookingDtoWithBookerId>> lastAndNextBooking = getLastAndNextBookingDtoWithBookerId(id, userId, item.getOwner().getId());
        return itemDtoMapper.mapToDto(item, lastAndNextBooking.get(0).orElse(null),
                lastAndNextBooking.get(1).orElse(null), comments);
    }

    @Override
    public ItemDto addItem(long ownerId, ItemDtoCreate itemDtoCreate) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isEmpty())
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage());
        return itemDtoMapper.mapToDto(itemRepository.save(itemDtoMapper.mapFromDtoCreate(itemDtoCreate, owner.get())),
                null, null, List.of());
    }

    @Override
    public List<ItemDto> getOwnerItems(long userId) {
        Map<Long,List<Booking>> bookingsMap = new HashMap<>();
        Map<Long,List<CommentDto>> commentsMap = new HashMap<>();

        bookingRepository.findByItemOwnerId(userId,Sort.by("start")).forEach(b -> bookingsMap.computeIfAbsent(b.getItem().getId(), k -> new ArrayList<>()).add(b));
        commentRepository.findByItemOwnerId(userId,Sort.by("created")).forEach(c -> commentsMap.computeIfAbsent(c.getItem().getId(), k -> new ArrayList<>()).add(commentDtoMapper.mapToDto(c)));

        return itemRepository.findByOwnerId(userId).stream().map(i -> {
            List<Optional<BookingDtoWithBookerId>> lastAndNextBooking = getLastAndNextBookingDtoWithBookerIdByList(bookingsMap.getOrDefault(i.getId(),List.of()));
            List<CommentDto> comments = commentsMap.getOrDefault(i.getId(), List.of());
            return itemDtoMapper.mapToDto(i, lastAndNextBooking.get(0).orElse(null), lastAndNextBooking.get(1).orElse(null), comments);
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
        List<CommentDto> comments = commentRepository.findByItemId(id, Sort.by("created")).stream().map(commentDtoMapper::mapToDto).collect(Collectors.toList());
        List<Optional<BookingDtoWithBookerId>> lastAndNextBooking = getLastAndNextBookingDtoWithBookerId(id, ownerId, item.getOwner().getId());
        return itemDtoMapper.mapToDto(itemRepository.save(item), lastAndNextBooking.get(0).orElse(null),
                lastAndNextBooking.get(1).orElse(null), comments);
    }

    @Override
    public List<ItemDto> searchItems(String text, long userId) {
        if (text.isBlank()) return List.of();
        return itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true)
                .stream()
                .map(i -> {
                    return itemDtoMapper.mapToDto(i, null, null, null);
                })
                .collect(Collectors.toList());
    }

    private List<Optional<BookingDtoWithBookerId>> getLastAndNextBookingDtoWithBookerId(long itemId, long userId, long ownerId) {

        if (ownerId != userId) return List.of(Optional.empty(), Optional.empty());
        List<Booking> bookings = bookingRepository.findByItemId(itemId, Sort.by("start"));
        return getLastAndNextBookingDtoWithBookerIdByList(bookings);
    }

    private List<Optional<BookingDtoWithBookerId>> getLastAndNextBookingDtoWithBookerIdByList(List<Booking> bookings) {

        List<Booking> bookingsStart = bookings.stream().filter(booking -> booking.getStart()
                .isBefore(LocalDateTime.now()) && booking.getStatus() == Status.APPROVED).collect(Collectors.toList());

        List<Booking> bookingsEnd = bookings.stream().filter(booking -> booking.getStart()
                .isAfter(LocalDateTime.now()) && booking.getStatus() == Status.APPROVED).collect(Collectors.toList());

        Optional<BookingDtoWithBookerId> last = bookingsStart.isEmpty() ? Optional.empty()
                : Optional.of(bookingMapper.mapToBookingDtoWithBookerId(bookingsStart.get(bookingsStart.size() - 1)));

        Optional<BookingDtoWithBookerId> next = bookingsEnd.isEmpty() ? Optional.empty()
                : Optional.of(bookingMapper.mapToBookingDtoWithBookerId(bookingsEnd.get(0)));
        return List.of(last, next);
    }


//    private void fillMaps() {
//        bookingsMap.clear();
//        commentMap.clear();
//        bookingRepository.findAll(Sort.by("start")).forEach(b -> bookingsMap.computeIfAbsent(b.getItem().getId(), k -> new ArrayList<>()).add(b));
//        commentRepository.findAll().forEach(c -> commentMap.computeIfAbsent(c.getItem().getId(), k -> new ArrayList<>()).add(commentDtoMapper.mapToDto(c)));
//    }

}
