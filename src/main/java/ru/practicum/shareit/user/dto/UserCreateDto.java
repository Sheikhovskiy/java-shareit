package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}
