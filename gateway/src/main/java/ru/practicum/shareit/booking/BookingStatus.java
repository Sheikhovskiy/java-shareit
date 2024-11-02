package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingStatus {

    APPROVED,

    REJECTED,

    CANCELED,

    WAITING;

    public static Optional<BookingStatus> from(String stringState) {
        for (BookingStatus status : values()) {
            if (status.name().equalsIgnoreCase(stringState)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}