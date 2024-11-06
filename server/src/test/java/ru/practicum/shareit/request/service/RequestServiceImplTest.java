package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional // Чтобы изменения откатывались
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class RequestServiceImplTest {

    private final EntityManager em;

    private final RequestService requestService;

    private Request requestTest;

    private User userTest;

    private final Request expectedDefaultRequest = new Request();

    private final Request expectedDefaultExistingDataRequest = new Request();

    private final String notExistingUser = "Ошибка создания запросов: Пользователь с идентификатором {} " +
            "не существует!";

    private final String notExistingRequest = "Ошибка создания запросов: Запрос на добавление предмета с идентификатором %d " +
            "не существует!";

    @BeforeEach
    void init() throws Exception {
        requestTest = new Request();
        requestTest.setDescription("requestDescriptionTest");

        userTest = new User();
        userTest.setId(11);
        userTest.setName("testFirstUser");
        userTest.setEmail("testFirstEmail");

        //
        expectedDefaultRequest.setId(1L);
        expectedDefaultRequest.setDescription("requestDescriptionTest");

        //
        expectedDefaultExistingDataRequest.setId(11L);
        expectedDefaultExistingDataRequest.setDescription("requestFirstDescription");
        expectedDefaultExistingDataRequest.setCreated(null);
        expectedDefaultExistingDataRequest.setOwner(userTest);
    }

    @Test
    @DisplayName("Создание запроса на добавление предмета")
    void testCreateRequest() throws Exception {

        long userTestId = 11L;
        long requestTestId = 1L;

        // when
        requestService.createRequest(requestTest, userTestId);

        TypedQuery<Request> query = em.createQuery("SELECT rq " +
                "FROM Request as rq " +
                "WHERE rq.id = :request_id", Request.class);
        Request requestReceived = query.setParameter("request_id", requestTestId).getSingleResult();

        RequestInfoDto requestInfoDtoReceived = makeRequestInfoDto(requestReceived);

        RequestInfoDto requestInfoDtoExpected = makeRequestInfoDto(expectedDefaultRequest);

        assertThat(requestInfoDtoReceived, allOf(
                hasProperty("id", equalTo(requestInfoDtoExpected.getId())),
                hasProperty("description", equalTo(requestInfoDtoExpected.getDescription()))
        ));
    }

    @Test
    @DisplayName("Создание запроса на добавление предмета с несуществующим пользователем")
    void testCreateRequestWithNotExistingUser() throws Exception {

        long userTestId = 111L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> requestService.createRequest(requestTest, userTestId)
        );

        assertEquals(String.format(notExistingUser, userTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Получение все запросы на добавление предмета по id пользователя")
    void testGetAllRequestsByUserId() throws Exception {

        long userTestId = 11L;

        List<Request> requestList = requestService.getAllRequestsByUserId(userTestId);
        List<RequestInfoDto> requestInfoDtoList = makeListRequestInfoDto(requestList);

        RequestInfoDto requestInfoDtoExpected = makeRequestInfoDto(expectedDefaultExistingDataRequest);

        assertThat(requestInfoDtoList, hasItems(allOf(
                hasProperty("id", equalTo(requestInfoDtoExpected.getId())),
                hasProperty("description", equalTo(requestInfoDtoExpected.getDescription())),
                hasProperty("created", equalTo(requestInfoDtoExpected.getCreated()))
        )));
    }

    @Test
    @DisplayName("Получение всех запросов на добавление предмета с несуществующим пользователем")
    void testGetAllRequestsByUserIdWithNotExistingUser() throws Exception {

        long userTestId = 111L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> requestService.getAllRequestsByUserId(userTestId)
        );

        assertEquals(String.format(notExistingUser, userTestId), thrownException.getMessage());
    }

    @Test
    @DisplayName("Получение всех запросов на добавление предмета")
    void testGetAllRequests() throws Exception {

        List<Request> requestListReceived = requestService.getAllRequests();

        List<RequestInfoDto> requestInfoDtoListReceived = makeListRequestInfoDto(requestListReceived);

        assertThat(requestInfoDtoListReceived, hasItems(
                allOf(
                        hasProperty("id", equalTo(11L)),
                        hasProperty("description", equalTo("requestFirstDescription")),
                        hasProperty("created", equalTo(null))
                ),
                allOf(
                        hasProperty("id", equalTo(22L)),
                        hasProperty("description", equalTo("requestSecondDescription")),
                        hasProperty("created", equalTo(null))
                ),
                allOf(
                        hasProperty("id", equalTo(33L)),
                        hasProperty("description", equalTo("requestThirdDescription")),
                        hasProperty("created", equalTo(null))
                ),
                allOf(
                        hasProperty("id", equalTo(44L)),
                        hasProperty("description", equalTo("requestFourthDescription")),
                        hasProperty("created", equalTo(null))
                ),
                allOf(
                        hasProperty("id", equalTo(55L)),
                        hasProperty("description", equalTo("requestFifthDescription")),
                        hasProperty("created", equalTo(null))
                )
        ));
    }

    @Test
    @DisplayName("Получение запроса на добавление предмета по id")
    void testGetRequestById() {

        long requestTestId = 11L;

        Request request = requestService.getRequestById(requestTestId);
        RequestInfoDto requestInfoDto = makeRequestInfoDto(request);

        assertThat(requestInfoDto, allOf(
                hasProperty("id", equalTo(requestTestId)),
                hasProperty("description", equalTo("requestFirstDescription")),
                hasProperty("created", equalTo(null))
        ));
    }

    @Test
    @DisplayName("Получение запроса на добавление предмета по id с несуществующим id запроса")
    void testGetRequestByIdWithNotExistentRequestId() {

        long requestTestId = 111L;

        NotFoundException thrownException = assertThrows(
                NotFoundException.class,
                () -> requestService.getRequestById(requestTestId)
        );

        assertEquals(String.format(notExistingRequest, requestTestId), thrownException.getMessage());
    }



    private static List<RequestInfoDto> makeListRequestInfoDto(List<Request> requestList) {

        return requestList.stream()
                .map(RequestServiceImplTest::makeRequestInfoDto)
                .collect(Collectors.toList());
    }

    private static RequestInfoDto makeRequestInfoDto(Request request) {
        RequestInfoDto requestInfoDto = new RequestInfoDto();

        requestInfoDto.setId(request.getId());
        requestInfoDto.setDescription(request.getDescription());
        requestInfoDto.setCreated(request.getCreated());
        if (request.getItemList() != null) {
            requestInfoDto.setItems(ItemMapper.toListItemDtoFromListItem(request.getItemList()));
        }

        return requestInfoDto;
    }




}