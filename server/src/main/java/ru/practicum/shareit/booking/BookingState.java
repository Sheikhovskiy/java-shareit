package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.ConditionsNotRespected;

public enum BookingState {

    ALL,

    CURRENT,

    FUTURE,

    PAST,

    REJECTED,

    WAITING;

    public static BookingState from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equalsIgnoreCase(state)) {
                return value;
            }
        }
        throw new ConditionsNotRespected(String.format("Состояния {} не существует", state));
    }

}