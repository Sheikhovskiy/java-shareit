package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ConstraintConstantsTest.HEADER_USER_ID;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Booking bookingTest;

    private User userTest;

    private Item itemTest;

    private static final String bookingDefaultResponse = "{\"id\":0,\"start\":null,\"end\":null,\"item\":{\"id\":0,\"name\":null,\"description\":null,\"available\":null,\"request\":null,\"requestId\":0},\"booker\":{\"id\":0,\"email\":null,\"name\":null},\"status\":\"WAITING\"}";

    private static final String bookingCreateResponse = "{\"id\":0,\"start\":null,\"end\":null,\"item\":{\"id\":11,\"name\":null,\"description\":null,\"available\":null,\"request\":null,\"requestId\":0},\"booker\":{\"id\":1,\"email\":null,\"name\":null},\"status\":\"WAITING\"}";


    @BeforeEach
    public void init() {
        bookingTest = new Booking();
        userTest = new User();
        itemTest = new Item();

        bookingTest.setBooker(userTest);
        bookingTest.setItem(itemTest);
        bookingTest.setStatus(BookingStatus.WAITING);
    }

    @Test
    @DisplayName("Создание бронирования")
    void testCreateBooking() throws Exception {

        long testUserId = 1L;
        long testItemId = 11L;

        userTest.setId(testUserId);
        itemTest.setId(testItemId);

        long userId = 1L;
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(11);
        bookingCreateDto.setStart(LocalDateTime.now().plusMinutes(5));
        bookingCreateDto.setEnd(LocalDateTime.now().plusMinutes(10));

        Booking bookingCreate = BookingMapper.toBookingFromBookingCreateDto(bookingCreateDto);

        String bookingCreateDtoJson = objectMapper.writeValueAsString(bookingCreateDto);

//        when(bookingService.createBooking(bookingCreate, bookingCreateDto.getItemId(), userId))
        when(bookingService.createBooking(Mockito.any(Booking.class), Mockito.eq(bookingCreateDto.getItemId()), Mockito.eq(userId)))
        .thenReturn(bookingTest);
        // Mockito.any(Booking.class) - Booking может быть любым объектом, так как мы не ожидаем конкретного экземпляра.
        // Mockito.eq(itemId) и Mockito.eq(userId) говорят, что для второго и третьего параметров мы ожидаем точное соответствие значениям itemId и userId.


        mockMvc.perform(post("/bookings")
                    .header(HEADER_USER_ID, userId)
                    .contentType("application/json")
                    .content(bookingCreateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingCreateResponse));

//        Mockito.verify(bookingService, times(1)).createBooking(bookingCreate, bookingCreateDto.getItemId(), userId);
        Mockito.verify(bookingService, times(1)).createBooking(Mockito.any(Booking.class), Mockito.eq(bookingCreateDto.getItemId()), Mockito.eq(userId));
    }


    @Test
    @DisplayName("Обновление бронирования по id бронирования")
    void testUpdateBooking() throws Exception {

        long userId = 1L;
        long bookingId = 2L;
        boolean approved = false;
        String path = "/bookings/" + bookingId;

        when(bookingService.updateBooking(bookingId, userId, approved))
                .thenReturn(bookingTest);

        mockMvc.perform(patch(path)
                    .header(HEADER_USER_ID, userId)
                    .param("approved", String.valueOf(approved)))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDefaultResponse));

        Mockito.verify(bookingService, times(1)).updateBooking(bookingId, userId, approved);
    }

    @Test
    @DisplayName("Получение бронирования по id бронирования")
    void testGetBookingById() throws Exception {
        long userId = 1L;
        long bookingId = 2L;
        String path = "/bookings/" + bookingId;

        when(bookingService.getBookingById(userId, bookingId))
                .thenReturn(bookingTest);

        mockMvc.perform(get(path)
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        Mockito.verify(bookingService, times(1)).getBookingById(userId, bookingId);
    }

    @Test
    @DisplayName("Получение всех бронирований предметов по id пользователя")
    void testGetAllBookings() throws Exception {

        String state = "past";
        long userId = 1L;

        when(bookingService.getAllBookingsByUserId(userId, String.valueOf(BookingState.PAST)))
        .thenReturn(Collections.emptyList());


        mockMvc.perform(get("/bookings")
                    .header(HEADER_USER_ID, userId)
                    .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        Mockito.verify(bookingService, times(1)).getAllBookingsByUserId(userId, "past");
    }

    @Test
    @DisplayName("Получение всех бронирований предметов по id владельца")
    void testGetAllBookingsOfOwner() throws Exception {

        String state = "past";
        long userId = 1L;

        when(bookingService.getAllBookingsByOwnerId(userId, String.valueOf(BookingState.PAST)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                    .header(HEADER_USER_ID, userId)
                    .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        Mockito.verify(bookingService, times(1)).getAllBookingsByOwnerId(userId, "past");

    }



}