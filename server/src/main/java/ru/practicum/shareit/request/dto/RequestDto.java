package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class RequestDto {

    private long id;

    private long ownerId;

    private String name;

    private String description;

    private Boolean isAvailable;

    private LocalDateTime bookedFrom;

    private LocalDateTime bookedUntil;
}
