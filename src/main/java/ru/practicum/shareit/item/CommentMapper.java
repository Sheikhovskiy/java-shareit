package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;

import java.util.List;

public class CommentMapper {

    public static Comment toCommentFromCommentCreateDto(CommentCreateDto commentCreateDto) {

        Comment comment = new Comment();

        comment.setText(commentCreateDto.getText());

        return comment;
    }

    public static CommentInfoDto toCommentInfoDtoFromComment(Comment comment) {

        CommentInfoDto commentInfoDto = new CommentInfoDto();

        commentInfoDto.setId(comment.getId());
        commentInfoDto.setText(comment.getText());
//        commentInfoDto.setItem(ItemMapper.toItemDtoFromItem(comment.getItem()));
        commentInfoDto.setAuthorName(comment.getAuthor().getName());
//        commentInfoDto.setAuthor(UserMapper.toUserDtoFromUser(comment.getAuthor()));
        commentInfoDto.setCreated(comment.getCreatedAt());

        return commentInfoDto;
    }


    public static List<CommentInfoDto> toListCommentInfoDtoFromListComment(List<Comment> commentList) {

        System.out.println("=====> " + commentList);

        return commentList.stream()
                .map(CommentMapper::toCommentInfoDtoFromComment)
                .toList();
    }

}
