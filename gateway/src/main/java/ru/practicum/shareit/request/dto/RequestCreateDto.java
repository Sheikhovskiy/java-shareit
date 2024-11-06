package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestCreateDto {


//    private long ownerId;

//    private String name;

    @NotBlank
    private String description;

//    private Boolean isAvailable;

//    private LocalDateTime bookedFrom;

//    private LocalDateTime bookedUntil;
}
