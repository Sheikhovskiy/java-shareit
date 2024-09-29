package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserIdDto {

    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;


}
