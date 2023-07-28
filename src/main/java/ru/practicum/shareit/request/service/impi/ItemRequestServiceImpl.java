package ru.practicum.shareit.request.service.impi;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.Messages;
import ru.practicum.shareit.exception.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoGet;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithListItem;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemDtoMapper itemDtoMapper;

    @Override
    public ItemRequestDtoGet addRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Messages.USER_NOT_FOUND.getMessage()));
        return itemRequestMapper.mapToDtoGet(itemRequestRepository.save(itemRequestMapper.mapFromDto(itemRequestDto, user)));

    }

    @Override
    public List<ItemRequestDtoWithListItem> getRequestsByRequestorId(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(userId, Sort.by("created").descending());
        List<ItemDtoWithRequestId> items = itemRepository
                .findByRequestIdIn(itemRequests.stream().map(ItemRequest::getId)
                        .collect(Collectors.toList())).stream().map(itemDtoMapper::mapToItemDtoWithRequestId).collect(Collectors.toList());

        return itemRequests.stream()
                .map(ir -> itemRequestMapper.mapToDtoWithListItem(ir, items.stream()
                        .filter(i -> i.getRequestId() == ir.getId())
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoGet> getAllRequests(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());
        return itemRequestRepository.findAllByRequestorIdNot(userId, pageable)
                .stream().map(itemRequestMapper::mapToDtoGet).collect(Collectors.toList());
    }


    @Override
    public ItemRequestDtoWithListItem getRequestById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException(Messages.ITEM_REQUEST_NOT_FOUND.getMessage()));
        List<ItemDtoWithRequestId> items = itemRepository.findByRequestIdIn(List.of(itemRequest.getId()))
                .stream().map(itemDtoMapper::mapToItemDtoWithRequestId).collect(Collectors.toList());
        return itemRequestMapper.mapToDtoWithListItem(itemRequest, items);
    }
}
