package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import java.util.List;


// Валидация нет в исходящих DTO
// Исходящее

@Data
public class
ItemCommentInfoDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private List<CommentInfoDto> comments;

    private BookingInfoDto lastBooking;

    private BookingInfoDto nextBooking;

}