package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class ItemUpdateDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private long owner;

    private ItemRequest request;


}
