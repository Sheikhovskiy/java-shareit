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

    private final Item expectedDefaultItem = new Item();

    private final Item expectedDefaultSecondItem = new Item();

    private final Request expectedDefaultRequest = new Request();

    private final Request expectedDefaultSecondRequest = new Request();

    private final Comment expectedDefaultComment = new Comment();

    private final String notExistingItem = "Ошибка при работе с предметами: Предмет с идентификатором %d " +
            "не существует!";

    private final String notExistingUser = "Ошибка при работе с предметами: Пользователь с идентификатором %d " +
            "не существует!";

    private final String inexistentBookingForUser = "Ошибка при работе с предметами: У пользователя с идентификатором %d," +
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

        expectedDefaultRequest.setId(11L);
        expectedDefaultRequest.setDescription("requestFirstDescription");
        expectedDefaultRequest.setCreated(null);
        expectedDefaultRequest.setOwner(userTest);

        expectedDefaultItem.setId(11);
        expectedDefaultItem.setName("testFirstItem");
        expectedDefaultItem.setDescription("testFirstItemDescription");
        expectedDefaultItem.setAvailable(true);
        expectedDefaultItem.setOwner(userTest);
        expectedDefaultItem.setRequest(expectedDefaultRequest);

        //

        User userTestSecond = new User();
        userTestSecond.setId(22L);
        userTestSecond.setName("testSecondUser");
        userTestSecond.setEmail("testSecondEmail");

        expectedDefaultSecondRequest.setId(22L);
        expectedDefaultSecondRequest.setDescription("requestSecondDescription");
        expectedDefaultSecondRequest.setCreated(null);
        expectedDefaultSecondRequest.setOwner(userTestSecond);

        expectedDefaultSecondItem.setId(22);
        expectedDefaultSecondItem.setName("testSecondItem");
        expectedDefaultSecondItem.setDescription("testSecondItemDescription");
        expectedDefaultSecondItem.setAvailable(true);
        expectedDefaultSecondItem.setOwner(userTestSecond);
        expectedDefaultSecondItem.setRequest(expectedDefaultSecondRequest);
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
        ItemDto itemDtoExpected = makeItemDto(expectedDefaultItem);

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

        assertEquals(String.format(notExistingItem, itemTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Получение всех предметов пользователя")
    void testGetAllItemsByUserId() throws Exception {

        long userTestId = 11L;

        List<Item> itemListReceived = itemService.getAllItemsByUserId(userTestId);


        List<ItemDto> itemListDtoReceived = makeListItemDto(itemListReceived);

        ItemDto itemDtoFirstExpected = makeItemDto(expectedDefaultItem);
        ItemDto itemDtoSecondExpected = makeItemDto(expectedDefaultSecondItem);


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
        ItemDto itemDtoExpected = makeItemDto(expectedDefaultItem);

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
        ItemDto itemDtoExpected = makeItemDto(expectedDefaultItem);

        assertThat(itemListDto, hasSize(0));
    }

    @Test
    @DisplayName("Создание комментария к предмету")
    void testCreateComment() throws Exception {

        User userTestBooked = new User();
        userTestBooked.setId(22L);
        userTestBooked.setName("testSecondUser");
        userTestBooked.setEmail("testSecondEmail");

        expectedDefaultComment.setId(1);
        expectedDefaultComment.setText("textTest");
        expectedDefaultComment.setAuthor(userTestBooked);
        expectedDefaultComment.setCreatedAt(null);

        long itemTestId = 11L;
        Comment commentReceived = itemService.createComment(commentTest, itemTestId, userTestBooked.getId());

        CommentInfoDto commentInfoDtoReceived = makeCommentInfoDto(commentReceived);
        CommentInfoDto commentInfoDtoExpected = makeCommentInfoDto(expectedDefaultComment);

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

        expectedDefaultComment.setId(1);
        expectedDefaultComment.setText("textTest");
        expectedDefaultComment.setAuthor(userTestBooked);
        expectedDefaultComment.setCreatedAt(null);

        long itemTestId = 111L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> itemService.createComment(commentTest, itemTestId, userTestId)
        );

        assertEquals(String.format(notExistingItem, itemTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Создание комментария к предмету с несуществующим пользователем")
    void testCreateCommentWithNonExistingUser() throws Exception {

        User userTestBooked = new User();

        long userTestId = 111L;

        userTestBooked.setId(userTestId);
        userTestBooked.setName("testSecondUser");
        userTestBooked.setEmail("testSecondEmail");

        expectedDefaultComment.setId(1);
        expectedDefaultComment.setText("textTest");
        expectedDefaultComment.setAuthor(userTestBooked);
        expectedDefaultComment.setCreatedAt(null);

        long itemTestId = 11L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> itemService.createComment(commentTest, itemTestId, userTestId)
        );

        assertEquals(String.format(notExistingUser, userTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Создание комментария к предмету с несуществующим пользователем")
    void testCreateCommentWithNonExistingBookingForUser() throws Exception {

        User userTestBooked = new User();

        long userTestId = 33L;

        userTestBooked.setId(userTestId);
        userTestBooked.setName("testSecondUser");
        userTestBooked.setEmail("testSecondEmail");

        expectedDefaultComment.setId(1);
        expectedDefaultComment.setText("textTest");
        expectedDefaultComment.setAuthor(userTestBooked);
        expectedDefaultComment.setCreatedAt(null);

        long itemTestId = 11L;

        ConditionsNotRespected thrownException = assertThrows(
                ConditionsNotRespected.class,
                () -> itemService.createComment(commentTest, itemTestId, userTestId)
        );

        assertEquals(String.format(inexistentBookingForUser, userTestId), thrownException.getMessage());
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
        if (item.getCommentsList() != null) {
            itemCommentInfoDto.setComments(CommentMapper.toListCommentInfoDtoFromListComment(item.getCommentsList()));
        }
        return itemCommentInfoDto;
    }


}