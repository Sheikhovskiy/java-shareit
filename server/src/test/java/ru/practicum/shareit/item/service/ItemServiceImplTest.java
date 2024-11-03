package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ConditionsNotRespected;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemCommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemServiceImplTest {

    private final EntityManager em;

    private final ItemService itemService;

    private Item itemTest;

    private User userTest;

    private Comment commentTest;

    private static final Item EXPECTED_DEFAULT_ITEM = new Item();

    private static final Item EXPECTED_DEFAULT_SECOND_ITEM = new Item();

    private static final Request EXPECTED_DEFAULT_REQUEST = new Request();

    private static final Request EXPECTED_DEFAULT_SECOND_REQUEST = new Request();

    private static final Comment EXPECTED_DEFAULT_COMMENT = new Comment();

    private static final String NOT_EXISTING_ITEM = "Ошибка при работе с предметами: Предмет с идентификатором %d " +
            "не существует!";

    private static final String NOT_EXISTING_USER = "Ошибка при работе с предметами: Пользователь с идентификатором %d " +
            "не существует!";

    private static final String INEXISTENT_BOOKING_FOR_USER = "Ошибка при работе с предметами: У пользователя с идентификатором %d," +
            "не существует бронирований предметов";

    @BeforeEach
    void init() {
        itemTest = new Item();
        userTest = new User();
        commentTest = new Comment();

        itemTest.setName("itemName");
        itemTest.setDescription("itemDescription");
        itemTest.setAvailable(true);
        itemTest.setOwner(userTest);
        itemTest.setRequest(null);

        userTest.setId(11);
        userTest.setName("testFirstUser");
        userTest.setEmail("user@testFirstEmail");

        commentTest.setText("textTest");

        //

        EXPECTED_DEFAULT_REQUEST.setId(11L);
        EXPECTED_DEFAULT_REQUEST.setDescription("requestFirstDescription");
        EXPECTED_DEFAULT_REQUEST.setCreated(null);
        EXPECTED_DEFAULT_REQUEST.setOwner(userTest);

        EXPECTED_DEFAULT_ITEM.setId(11);
        EXPECTED_DEFAULT_ITEM.setName("testFirstItem");
        EXPECTED_DEFAULT_ITEM.setDescription("testFirstItemDescription");
        EXPECTED_DEFAULT_ITEM.setAvailable(true);
        EXPECTED_DEFAULT_ITEM.setOwner(userTest);
        EXPECTED_DEFAULT_ITEM.setRequest(EXPECTED_DEFAULT_REQUEST);

        //

        User userTestSecond = new User();
        userTestSecond.setId(22L);
        userTestSecond.setName("testSecondUser");
        userTestSecond.setEmail("testSecondEmail");

        EXPECTED_DEFAULT_SECOND_REQUEST.setId(22L);
        EXPECTED_DEFAULT_SECOND_REQUEST.setDescription("requestSecondDescription");
        EXPECTED_DEFAULT_SECOND_REQUEST.setCreated(null);
        EXPECTED_DEFAULT_SECOND_REQUEST.setOwner(userTestSecond);

        EXPECTED_DEFAULT_SECOND_ITEM.setId(22);
        EXPECTED_DEFAULT_SECOND_ITEM.setName("testSecondItem");
        EXPECTED_DEFAULT_SECOND_ITEM.setDescription("testSecondItemDescription");
        EXPECTED_DEFAULT_SECOND_ITEM.setAvailable(true);
        EXPECTED_DEFAULT_SECOND_ITEM.setOwner(userTestSecond);
        EXPECTED_DEFAULT_SECOND_ITEM.setRequest(EXPECTED_DEFAULT_SECOND_REQUEST);
    }

    @Test
    @DisplayName("Создание предмета")
    void testCreateItem() throws Exception {

        long itemTestId = 1L;

        Item itemCreated = itemService.createItem(itemTest);
//        long itemTestId = itemCreated.getId();

        TypedQuery<Item> query = em.createQuery("SELECT it " +
                "FROM Item as it " +
                "WHERE it.id = : item_id", Item.class);
        Item itemReceived = query.setParameter("item_id", itemTestId).getSingleResult();

        ItemDto itemDtoReceived = makeItemDto(itemReceived);
        ItemDto itemDtoExpected = makeItemDto(itemCreated);

        assertThat(itemDtoReceived, allOf(
                hasProperty("id", equalTo(itemDtoExpected.getId())),
                hasProperty("name", equalTo(itemDtoExpected.getName())),
                hasProperty("description", equalTo(itemDtoExpected.getDescription())),
                hasProperty("available", equalTo(itemDtoExpected.getAvailable())),
                hasProperty("request", equalTo(itemDtoExpected.getRequest())),
                hasProperty("requestId", equalTo(itemDtoExpected.getRequestId()))
        ));
    }

    @Test
    @DisplayName("Создание предмета с несуществующим пользователем")
    void testCreateItemWithNonExistentUser() throws Exception {

        Item item = new Item();
        User user = new User();

        long userTestId = 111L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> itemService.createItem(item)
        );

        assertEquals("У каждого предмета должен быть владелец!", thrownException.getMessage());
    }

    @Test
    @DisplayName("Обновление предмета")
    void testUpdateItem() throws Exception {

        long itemNewTestId = 11L;
        itemTest.setId(itemNewTestId);
        itemTest.setName("RenamedTestName");

        // when
        itemService.updateItem(itemTest);

        TypedQuery<Item> query = em.createQuery("SELECT it " +
                "FROM Item as it " +
                "WHERE it.id = : item_id", Item.class);
        Item itemReceived = query.setParameter("item_id", itemNewTestId).getSingleResult();

        ItemDto itemDtoReceived = makeItemDto(itemReceived);
        ItemDto itemDtoExpected = makeItemDto(itemTest);

        assertThat(itemDtoReceived, allOf(
                hasProperty("id", equalTo(itemDtoExpected.getId())),
                hasProperty("name", equalTo(itemDtoExpected.getName())),
                hasProperty("description", equalTo(itemDtoExpected.getDescription())),
                hasProperty("available", equalTo(itemDtoExpected.getAvailable())),
                hasProperty("request", equalTo(itemDtoExpected.getRequest())),
                hasProperty("requestId", equalTo(itemDtoExpected.getRequestId()))
        ));
    }

    @Test
    @DisplayName("Создание предмета с несуществующим пользователем с несуществующим пользователем")
    void testUpdateItemWithNotExistingUser() throws Exception {

        Item item = new Item();

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> itemService.createItem(item)
        );

        assertEquals("У каждого предмета должен быть владелец!", thrownException.getMessage());
    }

    @Test
    @DisplayName("Получение предмета по id")
    void testGetItemById() {

        long itemTestId = 11L;
        // when
        itemService.getItemInfoById(itemTestId);

        TypedQuery<Item> query = em.createQuery("SELECT it " +
                "FROM Item as it " +
                "WHERE it.id = : item_id", Item.class);
        Item itemReceived = query.setParameter("item_id", itemTestId).getSingleResult();

        ItemDto itemDtoReceived = makeItemDto(itemReceived);
        ItemDto itemDtoExpected = makeItemDto(EXPECTED_DEFAULT_ITEM);

        assertThat(itemDtoReceived, allOf(
                hasProperty("id", equalTo(itemDtoExpected.getId())),
                hasProperty("name", equalTo(itemDtoExpected.getName())),
                hasProperty("description", equalTo(itemDtoExpected.getDescription())),
                hasProperty("available", equalTo(itemDtoExpected.getAvailable())),
                hasProperty("request", equalTo(itemDtoExpected.getRequest())),
                hasProperty("requestId", equalTo(itemDtoExpected.getRequestId()))
        ));
    }

    @Test
    @DisplayName("Получение предмета по id с несуществующим id предмета")
    void testGetItemByIdWithNonExistingItemId() {

        long itemTestId = 111L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> itemService.getItemInfoById(itemTestId)
        );

        assertEquals(String.format(NOT_EXISTING_ITEM, itemTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Получение всех предметов пользователя")
    void testGetAllItemsByUserId() throws Exception {

        long userTestId = 11L;

        List<Item> itemListReceived = itemService.getAllItemsByUserId(userTestId);


        List<ItemDto> itemListDtoReceived = makeListItemDto(itemListReceived);

        ItemDto itemDtoFirstExpected = makeItemDto(EXPECTED_DEFAULT_ITEM);
        ItemDto itemDtoSecondExpected = makeItemDto(EXPECTED_DEFAULT_SECOND_ITEM);


        assertThat(itemListDtoReceived, hasSize(2));

        assertThat(itemListDtoReceived, hasItems(allOf(
                hasProperty("id", equalTo(itemDtoFirstExpected.getId())),
                hasProperty("name", equalTo(itemDtoFirstExpected.getName())),
                hasProperty("description", equalTo(itemDtoFirstExpected.getDescription())),
                hasProperty("available", equalTo(itemDtoFirstExpected.getAvailable())),
                hasProperty("request", equalTo(itemDtoFirstExpected.getRequest())),
                hasProperty("requestId", equalTo(itemDtoFirstExpected.getRequestId()))
                ), allOf(
                hasProperty("id", equalTo(itemDtoSecondExpected.getId())),
                hasProperty("name", equalTo(itemDtoSecondExpected.getName())),
                hasProperty("description", equalTo(itemDtoSecondExpected.getDescription())),
                hasProperty("available", equalTo(itemDtoSecondExpected.getAvailable())),
                hasProperty("request", equalTo(itemDtoSecondExpected.getRequest())),
                hasProperty("requestId", equalTo(itemDtoSecondExpected.getRequestId()))
                )
        ));
    }

    @Test
    @DisplayName("Получение всех предметов по поиску")
    void testGetItemsBySearchRequest() throws Exception {

        String textTest = "First";
        long userTestId = 11L;

        List<Item> itemList = itemService.getItemsBySearchRequest(textTest, userTestId);

        List<ItemDto> itemListDto = makeListItemDto(itemList);
        ItemDto itemDtoExpected = makeItemDto(EXPECTED_DEFAULT_ITEM);

        assertThat(itemListDto, hasSize(1));

        assertThat(itemListDto, hasItem(allOf(
                hasProperty("id", equalTo(itemDtoExpected.getId())),
                hasProperty("name", equalTo(itemDtoExpected.getName())),
                hasProperty("description", equalTo(itemDtoExpected.getDescription())),
                hasProperty("available", equalTo(itemDtoExpected.getAvailable())),
                hasProperty("request", equalTo(itemDtoExpected.getRequest())),
                hasProperty("requestId", equalTo(itemDtoExpected.getRequestId()))
        )
        ));
    }

    @Test
    @DisplayName("Получение всех предметов по поиску")
    void testGetItemsByEmptySearchRequest() throws Exception {

        String textTest = "";
        long userTestId = 11L;

        List<Item> itemList = itemService.getItemsBySearchRequest(textTest, userTestId);

        List<ItemDto> itemListDto = makeListItemDto(itemList);
        ItemDto itemDtoExpected = makeItemDto(EXPECTED_DEFAULT_ITEM);

        assertThat(itemListDto, hasSize(0));
    }

    @Test
    @DisplayName("Создание комментария к предмету")
    void testCreateComment() throws Exception {

        User userTestBooked = new User();
        userTestBooked.setId(22L);
        userTestBooked.setName("testSecondUser");
        userTestBooked.setEmail("testSecondEmail");

        EXPECTED_DEFAULT_COMMENT.setId(1);
        EXPECTED_DEFAULT_COMMENT.setText("textTest");
        EXPECTED_DEFAULT_COMMENT.setAuthor(userTestBooked);
        EXPECTED_DEFAULT_COMMENT.setCreatedAt(null);

        long itemTestId = 11L;
        Comment commentReceived = itemService.createComment(commentTest, itemTestId, userTestBooked.getId());

        CommentInfoDto commentInfoDtoReceived = makeCommentInfoDto(commentReceived);
        CommentInfoDto commentInfoDtoExpected = makeCommentInfoDto(EXPECTED_DEFAULT_COMMENT);

        assertThat(commentInfoDtoReceived, allOf(
                hasProperty("id", equalTo(commentInfoDtoExpected.getId())),
                hasProperty("text", equalTo(commentInfoDtoExpected.getText())),
                hasProperty("authorName", equalTo(commentInfoDtoExpected.getAuthorName())),
                hasProperty("created", equalTo(commentInfoDtoExpected.getCreated()))
        ));
    }

    @Test
    @DisplayName("Создание комментария к предмету с несуществующим предметом")
    void testCreateCommentWithNonExistingItem() throws Exception {

        User userTestBooked = new User();
        long userTestId = 22L;

        userTestBooked.setId(userTestId);
        userTestBooked.setName("testSecondUser");
        userTestBooked.setEmail("testSecondEmail");

        EXPECTED_DEFAULT_COMMENT.setId(1);
        EXPECTED_DEFAULT_COMMENT.setText("textTest");
        EXPECTED_DEFAULT_COMMENT.setAuthor(userTestBooked);
        EXPECTED_DEFAULT_COMMENT.setCreatedAt(null);

        long itemTestId = 111L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> itemService.createComment(commentTest, itemTestId, userTestId)
        );

        assertEquals(String.format(NOT_EXISTING_ITEM, itemTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Создание комментария к предмету с несуществующим пользователем")
    void testCreateCommentWithNonExistingUser() throws Exception {

        User userTestBooked = new User();

        long userTestId = 111L;

        userTestBooked.setId(userTestId);
        userTestBooked.setName("testSecondUser");
        userTestBooked.setEmail("testSecondEmail");

        EXPECTED_DEFAULT_COMMENT.setId(1);
        EXPECTED_DEFAULT_COMMENT.setText("textTest");
        EXPECTED_DEFAULT_COMMENT.setAuthor(userTestBooked);
        EXPECTED_DEFAULT_COMMENT.setCreatedAt(null);

        long itemTestId = 11L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> itemService.createComment(commentTest, itemTestId, userTestId)
        );

        assertEquals(String.format(NOT_EXISTING_USER, userTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Создание комментария к предмету с несуществующим пользователем")
    void testCreateCommentWithNonExistingBookingForUser() throws Exception {

        User userTestBooked = new User();

        long userTestId = 33L;

        userTestBooked.setId(userTestId);
        userTestBooked.setName("testSecondUser");
        userTestBooked.setEmail("testSecondEmail");

        EXPECTED_DEFAULT_COMMENT.setId(1);
        EXPECTED_DEFAULT_COMMENT.setText("textTest");
        EXPECTED_DEFAULT_COMMENT.setAuthor(userTestBooked);
        EXPECTED_DEFAULT_COMMENT.setCreatedAt(null);

        long itemTestId = 11L;

        ConditionsNotRespected thrownException = assertThrows(
                ConditionsNotRespected.class,
                () -> itemService.createComment(commentTest, itemTestId, userTestId)
        );

        assertEquals(String.format(INEXISTENT_BOOKING_FOR_USER, userTestId), thrownException.getMessage());
    }



    private ItemDto makeItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        return itemDto;
    }

    private List<ItemDto> makeListItemDto(List<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::toItemDtoFromItem)
                .toList();
    }

    private CommentInfoDto makeCommentInfoDto(Comment comment) {
        CommentInfoDto commentInfoDto = new CommentInfoDto();

        commentInfoDto.setId(comment.getId());
        commentInfoDto.setText(comment.getText());
        commentInfoDto.setAuthorName(comment.getAuthor().getName());
        commentInfoDto.setCreated(comment.getCreatedAt());

        return commentInfoDto;
    }


    private ItemCommentInfoDto makeItemCommentInfoDto(Item item) {

        ItemCommentInfoDto itemCommentInfoDto = new ItemCommentInfoDto();

        itemCommentInfoDto.setId(item.getId());
        itemCommentInfoDto.setName(item.getName());
        itemCommentInfoDto.setDescription(item.getDescription());
        itemCommentInfoDto.setAvailable(item.getAvailable());
        if (item.getCommentList() != null) {
            itemCommentInfoDto.setComments(CommentMapper.toListCommentInfoDtoFromListComment(item.getCommentList()));
        }
        return itemCommentInfoDto;
    }


}