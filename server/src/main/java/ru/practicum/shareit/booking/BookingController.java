package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.shareit.CommonConstants;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingInfoDto createBooking(@RequestHeader(CommonConstants.HEADER_USER_ID) long bookerId,
                                        @RequestBody BookingCreateDto bookingCreateDto) {

        log.info("Получен запрос на бронирование вещи {}," +
                "от пользователя {}", bookingCreateDto, bookerId);
        Booking booking = bookingService.createBooking(BookingMapper.toBookingFromBookingCreateDto(bookingCreateDto), bookingCreateDto.getItemId(), bookerId);
        log.info("Бронирование успешно создано: {}", booking);
        return BookingMapper.toBookingInfoDtoFromBooking(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingInfoDto updateBooking(@RequestHeader(CommonConstants.HEADER_USER_ID) long ownerId,
                                        @PathVariable long bookingId,
                                        @RequestParam(name = "approved") boolean approvment) {

        log.info("Получен запрос на бронирование предмета по идентификатору {} от пользователя с идентификатором {} " +
                "с ответом: {}", bookingId, ownerId, approvment);
        Booking booking = bookingService.updateBooking(bookingId, ownerId, approvment);
        log.info("Статус бронирования предмета по идентификатором {} теперь {}", bookingId, booking.getStatus() != null ? booking.getStatus() : "");
        return BookingMapper.toBookingInfoDtoFromBooking(booking);
    }


    @GetMapping("/{bookingId}")
    public BookingInfoDto getBookingById(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                                         @PathVariable long bookingId) {

        log.info("Получен запрос о получении информации о бронирование предмета по идентификатору {} " +
                "от пользователя с идентификатором {} ", bookingId, userId);
        Booking booking = bookingService.getBookingById(userId, bookingId);
        log.info("Получена информации о бронирование предмета по идентификатору {}", bookingId);
        return BookingMapper.toBookingInfoDtoFromBooking(booking);
    }

    @GetMapping
    public List<BookingInfoDto> getAllBookingsByUserId(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                                                       @RequestParam(name = "state", defaultValue = "ALL") String state) {

        log.info("Получен запрос о получении информации о всех бронированиях предметом пользователя со статусом {}" +
                "от пользователя с идентификатормо {}", state, userId);
        List<Booking> bookingList = bookingService.getAllBookingsByUserId(userId, state);
        log.info("Получена информации о всех бронирований предметов пользователя по идентификатору {}", userId);
        return BookingMapper.toListBookingInfoDtoFromListBooking(bookingList);
    }

    @GetMapping("/owner")
    public List<BookingInfoDto> getAllBookingsOfOwner(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                                                      @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Получен запрос о получении информации о всех бронированиях предметом владельцем со статусом {}" +
                "от пользователя с идентификатормо {}", state, userId);
        List<Booking> bookingList = bookingService.getAllBookingsByOwnerId(userId, state);
        log.info("Получена информации о всех бронирований предметов владельца по идентификатору {}", userId);
        return BookingMapper.toListBookingInfoDtoFromListBooking(bookingList);

    }


}
