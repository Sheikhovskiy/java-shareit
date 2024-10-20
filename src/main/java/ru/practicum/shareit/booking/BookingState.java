package ru.practicum.shareit.booking;

public enum BookingState {

//    ALL,

    APPROVED,

//    CURRENT,

//    FUTURE,

//    PAST,

    REJECTED,

    CANCELED,

    WAITING;

    static BookingState from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }

}