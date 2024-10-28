package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestInfoDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

import static ru.practicum.shareit.CommonConstants.HEADER_USER_ID;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestInfoDto createRequest(@RequestHeader(HEADER_USER_ID) long userId,
                                        @RequestBody RequestCreateDto requestCreateDto) {

        log.info("Получен запрос от Клиента: Создание запроса на добавление вещи в аренду по описанию {} от пользователя с идентификатором {}", requestCreateDto, userId);
        Request request = requestService.createRequest(RequestMapper.toRequestFromRequestCreateDto(requestCreateDto), userId);
        log.info("Получен ответ: Создан запрос на добавление вещи в аренду {} от пользователя с идентификатором {}", request, userId);
        return RequestMapper.toRequestInfoDtoFromRequest(request);
    }

    @GetMapping
    public List<RequestInfoDto> getAllRequestsByUserId(@RequestHeader(HEADER_USER_ID) long userId) {

        log.info("Получен запрос от Клиента: Получение запрос на добавление вещи в аренду, созданные пользователем с идентификатором {}", userId);
        List<Request> requestList = requestService.getAllRequestsByUserId(userId);
        log.info("Получен ответ: Получен список всех запросов на создание предметов {} от пользователя с идентификатором {}", requestList, userId);
        return RequestMapper.toListRequestInfoDtoFromListRequest(requestList);
    }

    @GetMapping("/all")
    public List<RequestInfoDto> getAllExistingRequests(@RequestHeader(HEADER_USER_ID) long userId) {

        log.info("Получен запрос от Клиента: Получение запрос на получения всех запросов на получение всех предметов от пользователя с идентификатором {}", userId);
        List<Request> requestList = requestService.getAllRequests();
        log.info("Получен ответ: Получен список всех запросов на создание предметов {}", requestList);
        return RequestMapper.toListRequestInfoDtoFromListRequest(requestList);
    }

    @GetMapping("/{requestId}")
    public RequestInfoDto getRequestById(@PathVariable long requestId) {

        log.info("Получен запрос от Клиента: Получение запроса на просмотр данных о запросе на добавление предмета по идентификатору {}", requestId);
        Request request = requestService.getRequestById(requestId);
        log.info("Получен ответ: Получены данные о запросе на добавление предмета по идентификатору {}", request);
        return RequestMapper.toRequestInfoDtoFromRequest(request);
    }






}
