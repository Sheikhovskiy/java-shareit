package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CommentCreateDto {

    @NotBlank
    @Size(max = 128)
    private String text;

}
