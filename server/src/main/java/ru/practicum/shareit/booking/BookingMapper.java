package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@UtilityClass
public class BookingMapper {

    public BookingInfoDto toBookingInfoDtoFromBooking(Booking booking) {

        BookingInfoDto bookingInfoDto = new BookingInfoDto();
        bookingInfoDto.setId(booking.getId());
        bookingInfoDto.setStart(booking.getStart());
        bookingInfoDto.setEnd(booking.getEnd());
        if (booking.getItem() != null) {
            bookingInfoDto.setItem(ItemMapper.toItemDtoFromItem(booking.getItem()));
        }
        bookingInfoDto.setBooker(UserMapper.toUserDtoFromUser(booking.getBooker()));
        bookingInfoDto.setStatus(booking.getStatus());

        return bookingInfoDto;
    }

    public List<BookingInfoDto> toListBookingInfoDtoFromListBooking(List<Booking> bookingList) {

        return bookingList.stream()
                .map(BookingMapper::toBookingInfoDtoFromBooking)
                .toList();

    }


    public Booking toBookingFromBookingCreateDto(BookingCreateDto bookingCreateDto) {

        Booking booking = new Booking();

        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }

}
