package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ConstraintConstantsTest.HEADER_USER_ID;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc
class RequestControllerTest {


    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private final String requestDefaultResponse = "{\"id\":1,\"description\":requestTestDescription,\"created\":null,\"items\":null}";


    private Request requestTest;

    @BeforeEach
    void init() {
        requestTest = new Request();

        long requestTestId = 1L;
        String requestTestName = "requestTestName";
        String requestTestDescription = "requestTestDescription";

        requestTest.setId(requestTestId);
        requestTest.setDescription(requestTestDescription);
    }

    @Test
    @DisplayName("Создание запроса на добавление предмета")
    void testCreateRequest() throws Exception {

        long userTestId = 1L;
        String requestTestDescription = "requestTestDescription";

        RequestCreateDto requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription(requestTestDescription);

        String requestCreateDtoJson = objectMapper.writeValueAsString(requestCreateDto);

        when(requestService.createRequest(Mockito.any(Request.class), Mockito.eq(userTestId)))
                .thenReturn(requestTest);


        mockMvc.perform(post("/requests")
                        .header(HEADER_USER_ID, userTestId)
                        .contentType("application/json")
                        .content(requestCreateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(requestDefaultResponse));

        Mockito.verify(requestService, times(1)).createRequest(Mockito.any(Request.class), Mockito.eq(userTestId));
    }


    @Test
    @DisplayName("Получение запросов на создание предметов по id пользователя ")
    void testGetAllRequestsByUserId() throws Exception {

        long userTestId = 1L;

        when(requestService.getAllRequestsByUserId(userTestId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header(HEADER_USER_ID, userTestId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        Mockito.verify(requestService, times(1)).getAllRequestsByUserId(userTestId);
    }

    @Test
    @DisplayName("Получение всех существующих запросов на создание предметов")
    void testGetAllExistingRequests() throws Exception {

        long userTestId = 1L;

        when(requestService.getAllRequests())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header(HEADER_USER_ID, userTestId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        Mockito.verify(requestService, times(1)).getAllRequests();
    }

    @Test
    @DisplayName("Получени запроса на создание предметов по id")
    void testGetRequestById() throws Exception {

        long requestId = 3L;

        String path = "/requests/" + requestId;

        when(requestService.getRequestById(Mockito.anyLong()))
                .thenReturn(requestTest);

        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(content().json(requestDefaultResponse));

        Mockito.verify(requestService, times(1)).getRequestById(requestId);
    }

}