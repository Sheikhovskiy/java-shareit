package ru.practicum.shareit.exception;

public class ConditionsNotRespected extends RuntimeException {
    public ConditionsNotRespected(String message) {
        super(message);
    }
}
