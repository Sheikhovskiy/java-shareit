package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;


// Валидация нет в исходящих DTO
// Исходящее

@Data
public class ItemDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private RequestInfoDto request;

    private long requestId;

}
