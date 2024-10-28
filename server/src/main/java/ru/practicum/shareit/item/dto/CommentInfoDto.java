package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentInfoDto {

    private long id;

    private String text;

    private String authorName;

    private LocalDateTime created;





}
