package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

    private long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

}
