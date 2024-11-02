package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotRespected;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;


    private static final String NOT_EXISTING_ITEM = "Ошибка бронирования: Предмет с идентификатором {} " +
            "не существует!";

    private static final String NOT_EXISTING_USER = "Ошибка бронирования: Пользователь с идентификатором {} " +
            "не существует!";

    private static final String UNAVAILABLE_ITEM = "Ошибка бронирования: Предмет с идентификатором {} не доступен для бронирования";

    private static final String NOT_EXISTING_BOOKING = "Ошибка бронирования: Бронирование с идентификатором {} " +
            "не существует!";

    private static final String NOT_ITEM_OWNER = "Ошибка бронирования: Статус бронирования с идентификатором {} " +
            "может быть изменено только пользователем предмета бронирования!";

    private static final String NOT_AUTHORIZED_ACCESS_TO_BOOKING = "Ошибка бронирования: Получение информации о бронировании" +
            "с идентификатором {}, доступно только владельцу или арендатору";

    private static final String INEXISTENT_BOOKING_FOR_USER = "Ошибка бронирования: У пользователя с идентификатором {}, " +
            "не существует бронирований предметов";

    private static final String INEXISTENT_BOOKING_STATE = "Ошибка бронирования: Состояния {} не существует !";


    @Override
    public Booking createBooking(Booking booking, long itemId, long bookerId) {

        Optional<Item> requestedItemOpt = itemRepository.findById(itemId);
        Optional<User> bookerOpt = userRepository.findById(bookerId);

        if (requestedItemOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_ITEM, itemId));
        }

        if (bookerOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_USER, bookerId));
        }

        if (!requestedItemOpt.get().getAvailable()) {
            throw new ConditionsNotRespected(String.format(UNAVAILABLE_ITEM, itemId));
        }

        booking.setItem(requestedItemOpt.get());
        booking.setBooker(bookerOpt.get());

        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(long bookingId, long ownerId, boolean approvment) {

        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_BOOKING, bookingId));
        }

        Booking booking = bookingOpt.get();

        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new ConditionsNotRespected(String.format(NOT_ITEM_OWNER, ownerId));
        }

        if (approvment) {
            booking.setStatus(BookingStatus.APPROVED);
        }
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(long userId, long bookingId) {

        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_BOOKING, bookingId));
        }

        Booking booking = bookingOpt.get();

        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new ConditionsNotRespected(String.format(NOT_AUTHORIZED_ACCESS_TO_BOOKING, bookingId));
        }
        return booking;
    }

    public List<Booking> getAllBookingsByUserId(long userId, String state) {

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_USER, userId));
        }

        List<Optional<Booking>> bookingListOpt;

        switch (BookingState.from(state)) {
            case BookingState.ALL:
                bookingListOpt = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                break;
            case BookingState.CURRENT:
                bookingListOpt = bookingRepository.findByBooker_IdAndCurrent(userId, LocalDateTime.now());
                break;
            case BookingState.PAST:
                bookingListOpt = bookingRepository.findByBooker_IdAndDone(userId, LocalDateTime.now());
                break;
            case BookingState.FUTURE:
                bookingListOpt = bookingRepository.findByBooker_IdAndFuture(userId, LocalDateTime.now());
                break;
            case BookingState.WAITING:
                bookingListOpt = bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
                break;
            case BookingState.REJECTED:
                bookingListOpt = bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new NotFoundException(String.format(INEXISTENT_BOOKING_STATE, state));
        }

        List<Booking> bookingList = bookingListOpt.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        return bookingList;
    }

    @Override
    public List<Booking> getAllBookingsByOwnerId(long ownerId, String state) {

        Optional<User> userOpt = userRepository.findById(ownerId);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_USER, ownerId));
        }

        List<Optional<Booking>> bookingListOpt;

        switch (BookingState.from(state)) {
            case BookingState.ALL:
                bookingListOpt = bookingRepository.findByItemOwner(ownerId);
                break;
            case BookingState.CURRENT:
                bookingListOpt = bookingRepository.findByOwner_IdAndCurrent(ownerId, LocalDateTime.now());
                break;
            case BookingState.PAST:
                bookingListOpt = bookingRepository.findByOwner_IdAndDone(ownerId, LocalDateTime.now());
                break;
            case BookingState.FUTURE:
                bookingListOpt = bookingRepository.findByOwner_IdAndFuture(ownerId, LocalDateTime.now());
                break;
            case BookingState.WAITING:
                bookingListOpt = bookingRepository.findByOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
                break;
            case BookingState.REJECTED:
                bookingListOpt = bookingRepository.findByOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
                break;
            default:
                throw new NotFoundException(String.format(INEXISTENT_BOOKING_STATE, state));
        }

        List<Booking> bookingList = bookingListOpt.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        return bookingList;
    }






}
