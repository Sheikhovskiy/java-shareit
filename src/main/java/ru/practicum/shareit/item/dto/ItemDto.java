package ru.practicum.shareit.item.dto;

import lombok.Data;


// Валидация нет в исходящих DTO
// Исходящее

@Data
public class ItemDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

}
