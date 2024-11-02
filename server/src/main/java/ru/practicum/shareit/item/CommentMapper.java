package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;

import java.util.List;

@UtilityClass
public class CommentMapper {

    public Comment toCommentFromCommentCreateDto(CommentCreateDto commentCreateDto) {

        Comment comment = new Comment();

        comment.setText(commentCreateDto.getText());

        return comment;
    }

    public CommentInfoDto toCommentInfoDtoFromComment(Comment comment) {

        CommentInfoDto commentInfoDto = new CommentInfoDto();

        commentInfoDto.setId(comment.getId());
        commentInfoDto.setText(comment.getText());
        commentInfoDto.setAuthorName(comment.getAuthor().getName());
        commentInfoDto.setCreated(comment.getCreatedAt());

        return commentInfoDto;
    }


    public List<CommentInfoDto> toListCommentInfoDtoFromListComment(List<Comment> commentList) {

        return commentList.stream()
                .map(CommentMapper::toCommentInfoDtoFromComment)
                .toList();
    }


}


