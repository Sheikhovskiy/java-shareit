package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking, long itemId, long bookerId);

    Booking updateBooking(long bookingId, long ownerId, boolean approvment);

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getAllBookingsByUserId(long userId, String state);

    List<Booking> getAllBookingsByOwnerId(long ownerId, String state);


}
