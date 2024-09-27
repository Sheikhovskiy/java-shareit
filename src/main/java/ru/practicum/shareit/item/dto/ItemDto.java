package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

// Валидация нет в исходящих DTO
// Исходящее

@Data
public class ItemDto {

    private String name;

    private String description;

    private Boolean isAvailable;

    private ItemRequest request;
}
