package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ConstraintConstantsTest.HEADER_USER_ID;


@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ITEM_DEFAULT_CREATE_RESPONSE = "{\"id\":1,\"name\":testName,\"description\":testDescription,\"available\":true,\"request\":null,\"requestId\":3}";


    private Item itemTest;

    private User userTest;

    private Request requestTest;

    @BeforeEach
    public void init() {
        itemTest = new Item();
        userTest = new User();
        requestTest = new Request();

        long userTestId = 1L;
        long itemTestId = 1L;
        long requestRestId = 3L;
        boolean availableTest = true;

        userTest.setId(userTestId);
        itemTest.setId(itemTestId);
        itemTest.setName("testName");
        itemTest.setDescription("testDescription");
        requestTest.setId(requestRestId);

        itemTest.setAvailable(availableTest);
        itemTest.setOwner(userTest);
        itemTest.setRequest(requestTest);
    }

    @Test
    @DisplayName("Создание предмета")
    void testCreateItem() throws Exception {

        long userTestId = 1L;
//        long itemTestId = 2L;

        String testItemName = "testName";
        String testItemDescription = "testDescription";
        boolean testAvailable = true;
        long requestId = 3L;


        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName(testItemName);
        itemCreateDto.setDescription(testItemDescription);
        itemCreateDto.setAvailable(testAvailable);
        itemCreateDto.setRequestId(requestId);

        String itemCreateDtoJson = objectMapper.writeValueAsString(itemCreateDto);

        when(userService.getUserById(Mockito.anyLong()))
                .thenReturn(userTest);

        when(itemService.createItem(Mockito.any(Item.class)))
                .thenReturn(itemTest);

        mockMvc.perform(post("/items")
                .header(HEADER_USER_ID, userTestId)
                .contentType("application/json")
                .content(itemCreateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(ITEM_DEFAULT_CREATE_RESPONSE));

        Mockito.verify(userService, times(1)).getUserById(Mockito.anyLong());
        Mockito.verify(itemService, times(1)).createItem(Mockito.any(Item.class));

    }











}