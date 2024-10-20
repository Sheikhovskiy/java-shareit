package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class CommentCreateDto {

    @NotBlank
    private String text;

    @Positive
    @NotNull
    private long itemId;


}
