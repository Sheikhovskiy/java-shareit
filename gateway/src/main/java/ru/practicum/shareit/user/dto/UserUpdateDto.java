package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {

    private long id;

    @Size(max = 64)
    private String name;

    @Email
    @Size(max = 64)
    private String email;
}
