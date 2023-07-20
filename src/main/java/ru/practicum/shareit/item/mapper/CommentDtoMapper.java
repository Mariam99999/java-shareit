package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class CommentDtoMapper {
    public CommentDto mapToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());

    }

    public Comment mapFromDto(CommentDto commentDto, User user, Item item) {
        return new Comment(commentDto.getId(), commentDto.getText(), item, user, commentDto.getCreated());
    }
}