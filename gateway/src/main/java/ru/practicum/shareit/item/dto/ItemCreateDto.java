package ru.practicum.shareit.item.dto;

import io.micrometer.core.ipc.http.HttpSender;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.coyote.Request;

@Data
public class ItemCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private long owner;

    private long requestId;
}
