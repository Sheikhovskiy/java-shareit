package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemCreateDto {

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotBlank
    @Size(max = 128)
    private String description;

    @NotNull
    private Boolean available;

    private long owner;

    private long requestId;
}
