package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithListItem;
import ru.practicum.shareit.request.dto.ItemRequestDtoGet;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    @PostMapping
    private ItemRequestDtoGet addRequest(@RequestHeader("X-Sharer-User-Id") long userId, @Validated ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(userId,itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDtoWithListItem> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getRequestsByRequestorId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoGet> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        return itemRequestService.getAllRequests(userId,from,size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDtoWithListItem getRequestById(@PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestId);
    }


}
