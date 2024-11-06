package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public class BookingCreateDto {

    @Positive
    private long itemId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

}
