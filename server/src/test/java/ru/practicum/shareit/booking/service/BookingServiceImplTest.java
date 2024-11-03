package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.exception.ConditionsNotRespected;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional // Чтобы изменения откатывались сразу
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest // Интеграционные тесты - подтягивает контекст
//@Sql("classpath:data.sql")
class BookingServiceImplTest {

    private final EntityManager em;

    private final BookingService bookingService;

    private Booking bookingTest;

    private Item itemTest;

    private User userTest;

    private final String notExisingItem = "Ошибка бронирования: Предмет с идентификатором {} " +
            "не существует!";

    private final String notExistingUser = "Ошибка бронирования: Пользователь с идентификатором {} " +
            "не существует!";

    private final String unavailableItem = "Ошибка бронирования: Предмет с идентификатором {} не доступен для бронирования";

    private final String notExistingBooking = "Ошибка бронирования: Бронирование с идентификатором {} " +
            "не существует!";

    private final String notAuthorizedAccesToBooking = "Ошибка бронирования: Получение информации о бронировании" +
            "с идентификатором {}, доступно только владельцу или арендатору";


    @BeforeEach
    void init() {
        bookingTest = new Booking();

        userTest = new User();
        itemTest = new Item();

        long userTestId = 33L;
        long itemTestId = 33L;
        LocalDateTime startTest = null;
        LocalDateTime endTest = null;

        userTest.setId(userTestId);
        itemTest.setId(itemTestId);

        BookingStatus bookingTestStatus = BookingStatus.WAITING;

        bookingTest.setStart(startTest);
        bookingTest.setEnd(endTest);
        bookingTest.setItem(itemTest);
        bookingTest.setBooker(userTest);
        bookingTest.setStatus(bookingTestStatus);
    }

    @Test
    @DisplayName("Создание бронирования")
    void testCreateBooking() throws Exception {

        long bookingTestId = 1L;

        bookingService.createBooking(bookingTest, bookingTest.getItem().getId(), bookingTest.getBooker().getId());

        TypedQuery<Booking> query = em.createQuery("SELECT bk " +
                "FROM Booking as bk " +
                "WHERE bk.id = :booking_id", Booking.class);
        Booking booking = query.setParameter("booking_id", bookingTestId).getSingleResult();

        BookingInfoDto bookingInfoDto = makeBookingInfoDto(booking);
        BookingInfoDto expectedBookingInfoDto = makeBookingInfoDto(bookingTest);

        assertThat(bookingInfoDto, allOf(
                hasProperty("id", equalTo(expectedBookingInfoDto.getId())),
                hasProperty("start", equalTo(expectedBookingInfoDto.getStart())),
                hasProperty("end", equalTo(expectedBookingInfoDto.getEnd())),
                hasProperty("item", allOf(
                        hasProperty("id", equalTo(expectedBookingInfoDto.getItem().getId())

                ))),
                hasProperty("booker", allOf(
                        hasProperty("id", equalTo(expectedBookingInfoDto.getBooker().getId()))
                )),
                hasProperty("status", equalTo(expectedBookingInfoDto.getStatus()))
        ));
    }

    @Test
    @DisplayName("Создание бронирования")
    void testCreateBookingWithNonExistingItem() throws Exception {

        Booking booking =  new Booking();

        User user = new User();
        Item item = new Item();

        long userTestId = 33L;
        long itemTestId = 88L;

        booking.setStart(null);
        booking.setEnd(null);

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> bookingService.createBooking(booking, itemTestId, userTestId)
        );

        assertEquals(String.format(notExisingItem, itemTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Создание бронирования")
    void testCreateBookingWithNonExistingOwner() throws Exception {

        Booking booking =  new Booking();

        User user = new User();
        Item item = new Item();

        long userTestId = 88L;
        long itemTestId = 11L;

        booking.setStart(null);
        booking.setEnd(null);

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> bookingService.createBooking(booking, itemTestId, userTestId)
        );

        assertEquals(String.format(notExistingUser, itemTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Создание бронирования")
    void testCreateBookingWithUnavailable() throws Exception {

        Booking booking =  new Booking();

        User user = new User();
        Item item = new Item();

        long userTestId = 11L;
        long itemTestId = 55L;

        user.setId(userTestId);
        item.setId(itemTestId);
        item.setAvailable(false);

        booking.setStart(null);
        booking.setEnd(null);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        ConditionsNotRespected thrownException = assertThrows(
                ConditionsNotRespected.class,
                () -> bookingService.createBooking(booking, itemTestId, userTestId)
        );

        assertEquals(String.format(unavailableItem, itemTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Обновление бронирования")
    void testUpdateBooking() throws Exception {

        long bookingTestId = 11L;
        long ownerTestId = 11L;
        long bookerTestId = 22L;
        boolean approvmentTest = true;

        bookingService.updateBooking(bookingTestId, ownerTestId, approvmentTest);

        TypedQuery<Booking> query = em.createQuery("SELECT bk " +
                "FROM Booking as bk " +
                "WHERE bk.id = : booking_id", Booking.class);
        Booking booking = query.setParameter("booking_id", bookingTestId).getSingleResult();

        BookingInfoDto bookingInfoDto = makeBookingInfoDto(booking);

        assertThat(bookingInfoDto, allOf(
                hasProperty("id", equalTo(bookingTestId)),
                hasProperty("booker", allOf(
                        hasProperty("id", equalTo(bookerTestId))
                )),
                hasProperty("status", equalTo(BookingStatus.APPROVED))
        ));
    }

    @Test
    @DisplayName("Получить бронирование по id")
    void testGetBookingById() throws Exception {

        long bookingId = 11L;
        long itemId = 11L;
        long bookerId = 22L;
        long requestId = 11L;

        Booking booking = bookingService.getBookingById(bookerId, bookingId);
        BookingInfoDto bookingInfoDto = makeBookingInfoDto(booking);

        assertThat(bookingInfoDto, allOf(
                hasProperty("id", equalTo(bookingId)),
//                hasProperty("start", '2024-11-01 14:11:14.0'),
//                hasProperty("end", nullValue()),
                hasProperty("item", allOf(
                        hasProperty("id", equalTo(itemId)),
                        hasProperty("name", equalTo("testFirstItem")),
                        hasProperty("description", equalTo("testFirstItemDescription")),
                        hasProperty("available", equalTo(true)),
                        hasProperty("requestId", equalTo(requestId))
                )),
                hasProperty("booker", allOf(
                        hasProperty("id", equalTo(bookerId)),
                        hasProperty("email", equalTo("testSecondEmail")),
                        hasProperty("name", equalTo("testSecondUser"))
                )),
                hasProperty("status", equalTo(BookingStatus.APPROVED))
        ));
    }

    @Test
    @DisplayName("Получить бронирование по id")
    void testGetBookingByIdWithNotAuthorizedAccess() throws Exception {

        long userTestId = 77L;
        long bookingTestId = 11L;

        ConditionsNotRespected thrownException = assertThrows(
                ConditionsNotRespected.class,
                () -> bookingService.getBookingById(userTestId, bookingTestId)
        );

        assertEquals(String.format(notAuthorizedAccesToBooking, userTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Получить бронирование по id")
    void testGetBookingByIdWithNotExistingBooking() throws Exception {

        long userTestId = 11L;
        long bookingTestId = 99L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingById(userTestId, bookingTestId)
        );

        assertEquals(String.format(notExistingBooking, bookingTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом ALL")
    void testGetAllBookingsByUserIdAndAll() throws Exception {

        long userTestUserId = 11L;
        String stateTest = "ALL";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByUserId(userTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", nullValue()),
                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом CURRENT")
    void testGetAllBookingsByUserIdAndCurrent() throws Exception {

        long userTestUserId = 11L;
        String stateTest = "CURRENT";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByUserId(userTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", nullValue()),
                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом FUTURE")
    void testGetAllBookingsByUserIdAndFuture() throws Exception {

        long userTestUserId = 11L;
        String stateTest = "FUTURE";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByUserId(userTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", nullValue()),
                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом PAST")
    void testGetAllBookingsByUserIdAndPast() throws Exception {

        long userTestUserId = 11L;
        String stateTest = "PAST";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByUserId(userTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", nullValue()),
                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом REJECTED")
    void testGetAllBookingsByUserIdAndRejected() throws Exception {

        long userTestUserId = 11L;
        String stateTest = "REJECTED";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByUserId(userTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", nullValue()),
                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом WAITING")
    void testGetAllBookingsByUserIdAndWaiting() throws Exception {

        long userTestUserId = 11L;
        String stateTest = "WAITING";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByUserId(userTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", nullValue()),
                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом ALL и несуществующим пользователем")
    void testGetAllBookingsByUserIdWithNonExistingUser() throws Exception {

        long userTestId = 77L;
        long bookingTestId = 11L;
        String stateTest = "ALL";

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> bookingService.getAllBookingsByUserId(userTestId, stateTest)
        );
        assertEquals(String.format(notExistingUser, userTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом ALL")
    void testGetAllBookingsByOwnerIdAndAll() throws Exception {

        long ownerTestUserId = 11L;
        String stateTest = "ALL";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByOwnerId(ownerTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
//                    hasProperty("start", nullValue()),
//                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом CURRENT")
    void testGetAllBookingsByOwnerIdAndCurrent() throws Exception {

        long ownerTestUserId = 11L;
        String stateTest = "CURRENT";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByOwnerId(ownerTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
//                    hasProperty("start", nullValue()),
//                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом FUTURE")
    void testGetAllBookingsByOwnerIdAndFuture() throws Exception {

        long ownerTestUserId = 11L;
        String stateTest = "FUTURE";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByOwnerId(ownerTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
//                    hasProperty("start", nullValue()),
//                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом PAST")
    void testGetAllBookingsByOwnerIdAndPast() throws Exception {

        long ownerTestUserId = 11L;
        String stateTest = "PAST";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByOwnerId(ownerTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
//                    hasProperty("start", nullValue()),
//                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом REJECTED")
    void testGetAllBookingsByOwnerIdAndRejected() throws Exception {

        long ownerTestUserId = 11L;
        String stateTest = "REJECTED";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByOwnerId(ownerTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
//                    hasProperty("start", nullValue()),
//                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом WAITING")
    void testGetAllBookingsByOwnerIdAndWaiting() throws Exception {

        long ownerTestUserId = 11L;
        String stateTest = "WAITING";

        List<Booking> bookingListReceived = bookingService.getAllBookingsByOwnerId(ownerTestUserId, stateTest);

        List<BookingInfoDto> bookingInfoDtoListReceived = makeListBookingInfoDto(bookingListReceived);

        for (BookingInfoDto bookingInfoDto : bookingInfoDtoListReceived) {

            assertThat(bookingInfoDto, allOf(
                    hasProperty("id", notNullValue()),
//                    hasProperty("start", nullValue()),
//                    hasProperty("end", nullValue()),
                    hasProperty("item", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("name", notNullValue()),
                            hasProperty("description", notNullValue()),
                            hasProperty("available", notNullValue())
                    )),
                    hasProperty("booker", allOf(
                            hasProperty("id", notNullValue()),
                            hasProperty("email", notNullValue()),
                            hasProperty("name", notNullValue())
                    )),
                    hasProperty("status", notNullValue())
            ));
        }
    }

    @Test
    @DisplayName("Получение всех бронирований по id пользователя со статусом ALL и несуществующем пользователем")
    void testGetAllBookingsByOwnerIdWithNonExistingUser() throws Exception {

        long userTestId = 77L;
        String stateTest = "ALL";

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> bookingService.getAllBookingsByOwnerId(userTestId, stateTest)
        );
        assertEquals(String.format(notExistingUser, userTestId), thrownException.getMessage());
    }

//    @Test
//    void getAllBookings() {
//        long userId = 1L;
//        BookingState bookingState = BookingState.ALL;
//
//        List<Booking> bookings = bookingService.getAllBookingsByOwnerId(userId, String.valueOf(bookingState));
//
//        // 1. data.sql + константы + статические методы
//        assertEquals(getTestBooking(), bookings);
//        // 2. Создаем данные предварительно через em, получаем ожидаемые данные через em (если изменяем, создаем, удаляем)
//        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.userId = :userId", Booking.class);
//        query.setParameter("userId", userId);
//        List<Booking> resultList = query.getResultList();
//        List<BookingInfoDto> expected = BookingMapper.toListBookingInfoDtoFromListBooking(resultList);
//        assertEquals(expected, bookings);
//
//
//        // Создание
//        // через em по id находим Booking
//        // По каждому полю сравниваем Booking и BookingDto
//    }

//    private List<Booking> getTestBooking() {
//        return List.of(new Booking());
//    }


    private static BookingInfoDto makeBookingInfoDto(Booking booking) {

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


    private static List<BookingInfoDto> makeListBookingInfoDto(List<Booking> bookingList) {

        return bookingList.stream()
                .map(BookingServiceImplTest::makeBookingInfoDto)
                .collect(Collectors.toList());
    }



}