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
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private final String itemDefaultCreateResponse = "{\"id\":1,\"name\":testItemName,\"description\":testDescription,\"available\":true,\"request\":null,\"requestId\":3}";

    private final String itemDefaultWithCommentResponse = "{\"id\":1,\"name\":testItemName,\"description\":testDescription,\"available\":true,\"comments\":null,\"lastBooking\":null,\"nextBooking\":null}";

    private final String itemDefaultCommentResponse = "{\"id\":1,\"text\":\"commentTestText\",\"authorName\":testUserName,\"created\":null}";

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

        userTest.setName("testUserName");
        userTest.setId(userTestId);
        itemTest.setId(itemTestId);
        itemTest.setName("testItemName");
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

        String testItemName = "testItemName";
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
                .andExpect(content().json(itemDefaultCreateResponse));

        Mockito.verify(userService, times(1)).getUserById(Mockito.anyLong());
        Mockito.verify(itemService, times(1)).createItem(Mockito.any(Item.class));
    }

    @Test
    @DisplayName("Обновление предмета")
    void testUpdateItem() throws Exception {

        long userTestId = 1L;

        long itemTestId = 1L;
        String testItemName = "testItemName";
        String testItemDescription = "testDescription";
        boolean testAvailable = true;

        String path = "/items/" + itemTestId;

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
//        itemUpdateDto.setId(itemTestId);
        itemUpdateDto.setName(testItemName);
        itemUpdateDto.setDescription(testItemDescription);
        itemUpdateDto.setAvailable(testAvailable);
        itemUpdateDto.setOwner(userTestId);

        String itemUpdateDtoJson = objectMapper.writeValueAsString(itemUpdateDto);

        when(userService.getUserById(Mockito.anyLong()))
                .thenReturn(userTest);

        when(itemService.updateItem(Mockito.any(Item.class)))
                .thenReturn(itemTest);


        mockMvc.perform(patch(path)
                .header(HEADER_USER_ID, userTestId)
                .contentType("application/json")
                .content(itemUpdateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDefaultCreateResponse));

        Mockito.verify(userService, times(1)).getUserById(Mockito.anyLong());
        Mockito.verify(itemService, times(1)).updateItem(Mockito.any(Item.class));
    }


    @Test
    @DisplayName("Получение предмета по id предмета")
    void testGetItemById() throws Exception {

        long itemTestId = 1L;

        String path = "/items/" + itemTestId;

        when(itemService.getItemInfoById(Mockito.anyLong()))
                .thenReturn(itemTest);

        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDefaultWithCommentResponse));

        Mockito.verify(itemService, times(1)).getItemInfoById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Получение всех предметов пользователя")
    void testGetAllItemsByUserId() throws Exception {

        long userTestId = 1L;

        when(itemService.getAllItemsByUserId(userTestId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                 .header(HEADER_USER_ID, userTestId))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));

        Mockito.verify(itemService, times(1)).getAllItemsByUserId(userTestId);
    }

    @Test
    @DisplayName("Получение рекомендуемых предметов")
    void testGetRecommendedItems() throws Exception {

        long userTestId = 1L;
        String searchText = "searchText";


        when(itemService.getItemsBySearchRequest(searchText, userTestId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                .header(HEADER_USER_ID, userTestId)
                .param("text", searchText))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        Mockito.verify(itemService, times(1)).getItemsBySearchRequest(searchText, userTestId);
    }

    @DisplayName("Создание комментария")
    @Test
    void testCreateComment() throws Exception {

        long userTestId = 1L;
        long itemTestId = 1L;

        String commentTestText = "commentTestText";

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText(commentTestText);

        String commentCreateDtoJson = objectMapper.writeValueAsString(commentCreateDto);

        String path = "/items/" + itemTestId + "/comment";

        Comment commentResult = new Comment();
        commentResult.setId(itemTest.getId());
        commentResult.setItem(itemTest);
        commentResult.setText(commentTestText);
        commentResult.setAuthor(userTest);
        commentResult.setCreatedAt(null);

        when(itemService.createComment(Mockito.any(Comment.class), Mockito.eq(itemTestId), Mockito.eq(userTestId)))
                .thenReturn(commentResult);

        mockMvc.perform(post(path)
                .header(HEADER_USER_ID, userTestId)
                .contentType("application/json")
                .content(commentCreateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDefaultCommentResponse));

        Mockito.verify(itemService, times(1)).createComment(Mockito.any(Comment.class), Mockito.eq(itemTestId), Mockito.eq(userTestId));


    }






}