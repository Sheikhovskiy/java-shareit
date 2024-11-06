package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemUpdateDto {

    private long id;

    @Size(max = 64)
    private String name;

    @Size(max = 128)
    private String description;

    private Boolean available;

    private long owner;

}
