//package ru.practicum.shareit.booking.service;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.practicum.shareit.booking.Booking;
//import ru.practicum.shareit.booking.BookingMapper;
//import ru.practicum.shareit.booking.BookingState;
//import ru.practicum.shareit.booking.dto.BookingInfoDto;
//
//import java.awt.print.Book;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@Transactional // Чтобы изменения откатывались сразу
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@SpringBootTest // Интеграционные тесты - подтягивает контекст
//class BookingServiceImplTest {
//
//    private final EntityManager em;
//
//    private final BookingService bookingService;
//
////    private final BookingServiceImpl bookingServiceImpl;
//
//
//    @Test
//    void getAllBookings() {
//        long userId = 1L;
//        BookingState bookingState = BookingState.ALL;
//
//        List<Booking> bookings = bookingService.getAllBookingsByOwnerId(userId, String.valueOf(bookingState));
//
//        // 1. data.sql + константы + статиченеские методы
//        assertEquals(getTestBooking(), bookings);
//        // 2. Создаем данные предварительно через em, получаем ожидаемые данные через em (если изменяем, создаем, удаляем)
//        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.userId = :userId", Booking.class);
//        query.setParameter("userId", userId);
//        List<Booking> resultList = query.getResultList();
//        List<BookingInfoDto> expected = BookingMapper.toListBookingInfoDtoFromListBooking(resultList);
//        assertEquals(expected, bookings);
//
//        // Создание
//        // через em по id находим Booking
//        // По каждому полю сравниваем Booking и BookingDto
//    }
//
//
//
//    private List<Booking> getTestBooking() {
//        return List.of(new Booking());
//    }
//
//
//
//
//
//
//
//}