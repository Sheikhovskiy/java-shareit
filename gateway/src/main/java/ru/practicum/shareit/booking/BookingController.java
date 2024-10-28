package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.CommonConstants;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Optional;

import static ru.practicum.shareit.CommonConstants.HEADER_USER_ID;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader(HEADER_USER_ID) long userId,
										   @RequestBody @Valid BookingCreateDto bookingCreateDto) {

		log.info("Получен запрос на бронирование вещи {}," +
				"от пользователя {}", bookingCreateDto, userId);
		return bookingClient.createBooking(userId, bookingCreateDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBooking(@RequestHeader(HEADER_USER_ID) long ownerId,
										@PathVariable long bookingId,
										@RequestParam(name = "approved") boolean approvment) {

		log.info("Получен запрос на бронирование предмета по идентификатору {} от пользователя с идентификатором {} " +
				"с ответом: {}", bookingId, ownerId, approvment);
//		Booking booking = bookingService.updateBooking(bookingId, ownerId, approvment);
//		log.info("Статус бронирования предмета по идентификатором {} теперь {}", bookingId, booking.getStatus());
//		log.info("БРОНИРОВАНИЕ {}", booking);
//		return BookingMapper.toBookingInfoDtoFromBooking(booking);

//		BookingStatus status = BookingStatus.APPROVED;
//		if (!approvment) {
//			status = BookingStatus.REJECTED;
//		}

		return bookingClient.updateBooking(ownerId, bookingId, approvment);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
												 @PathVariable Long bookingId) {

		log.info("Получен запрос на получение бронирования {}, userId={}", bookingId, userId);
		return bookingClient.getBookingById(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Неизвестное состояние: " + stateParam));

		log.info("Получен запрос на получение бронирования с состоянием {}, userId={}, от={}, размер={}", stateParam, userId, from, size);

		return bookingClient.getAllBookingsByUserId(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllBookingsOfOwner(@RequestHeader(HEADER_USER_ID) long userId,
													  @RequestParam(name = "state", defaultValue = "ALL") String state) {

		log.info("Получен запрос о получении информации о всех бронированиях предметом владельцем со статусом {}" +
				"от пользователя с идентификатормо {}", state, userId);
//		List<Booking> bookingList = bookingService.getAllBookingsByOwnerId(userId, state);
//		log.info("Получена информации о всех бронирований предметов владельца по идентификатору {}", userId);
//		return BookingMapper.toListBookingInfoDtoFromListBooking(bookingList);


		Optional<BookingState> bookingStateOpt = BookingState.from(state);

		if (bookingStateOpt.isEmpty()) {
			throw new IllegalArgumentException("Состояние бронирования неизвестно");
		}

		return bookingClient.getAllBookingsOfOwner(userId, bookingStateOpt.get());
	}





}
