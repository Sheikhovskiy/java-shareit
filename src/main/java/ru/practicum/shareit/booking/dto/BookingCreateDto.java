package ru.practicum.shareit.booking.dto;

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
    @NotNull
    long itemId;

//    @FutureOrPresent
    LocalDateTime start;

//    @Future
    LocalDateTime end;

    long booker;

}
