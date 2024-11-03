package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestCreateDto;

import static ru.practicum.shareit.CommonConstants.HEADER_USER_ID;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(HEADER_USER_ID) long userId,
                                                @RequestBody @Valid RequestCreateDto requestCreateDto) {

        log.info("Получен запрос на создание запроса на аренду вещи по описанию {} от пользователя с идентификатором {}", requestCreateDto, userId);
        return requestClient.createRequest(userId, requestCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByUserId(@RequestHeader(HEADER_USER_ID) long userId) {

        log.info("Получение запрос на добавление вещи в аренду, созданные пользователем с идентификатором {}", userId);
        return requestClient.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllExistingRequests(long userId) {

        log.info("Получение запрос на получения всех запросов на получение всех предметов от пользователя с идентификатором {}", userId);
        return requestClient.getAllExistingRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable long requestId) {

        log.info("Получение запроса на просмотр данных о запросе на добавление предмета по идентификатору {}", requestId);
        return requestClient.getRequestById(requestId);
    }






}
