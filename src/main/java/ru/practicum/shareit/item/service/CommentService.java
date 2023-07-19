package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    List<CommentDto> getCommentsByItemId(Long itemId);
}
