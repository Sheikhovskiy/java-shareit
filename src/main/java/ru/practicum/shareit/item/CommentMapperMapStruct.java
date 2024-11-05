package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;

import java.util.List;

@Mapper
public interface CommentMapperMapStruct {

    CommentMapperMapStruct INSTANCE = Mappers.getMapper(CommentMapperMapStruct.class);

    Comment toCommentFromCommentCreateDto(CommentCreateDto commentCreateDto);

    CommentInfoDto toCommentInfoDtoFromComment(Comment comment);

    List<CommentInfoDto> toListCommentInfoDtoFromListComment(List<Comment> commentList);


}
