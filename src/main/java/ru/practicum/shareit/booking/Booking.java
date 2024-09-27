package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@EqualsAndHashCode(of = "id")
public class Booking {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private long booker;

    private BOOKING_STATUS status;

    enum BOOKING_STATUS {
        APPROVED,
        WAITING,
        REJECTED,
        CANCELED
    }


}
