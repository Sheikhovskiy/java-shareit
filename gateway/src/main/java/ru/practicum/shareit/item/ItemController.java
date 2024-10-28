package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.CommonConstants;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.UserClient;

/**
 *  PUT - обновление объекта целиком, PATCH - обновление поля объекта, можно и методом PUT обновить одно поле,
 *  однако метод PUT будет проходить все поля объекта и искать необходимое, в отличие от PATCH, который не обходит объект целиком
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                                     @RequestBody @Valid ItemCreateDto itemCreateDto) {

        log.info("Получен предмет на создание {}", itemCreateDto);
        itemCreateDto.setOwner(userId);
        return itemClient.createItem(userId, itemCreateDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
                              @RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                              @RequestBody @Valid ItemUpdateDto itemUpdateDto) {

        log.info("Получен предмет на обновление {}, itemId => {}, userId => {}", itemUpdateDto, itemId, userId);
        return itemClient.updateItem(itemId, userId, itemUpdateDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId) {

        log.info("Получен идентификатор предмета {}", itemId);
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId) {

        log.info("Получен идентификатор пользователя {} для получения всех фильмов пользователя", userId);
//        List<Item> itemsList = itemService.getAllItemsByUserId(userId);
//        log.info("Получен список всех предметов пользователя с идентификатором {} - {}", userId, itemsList);
//        return ItemMapper.toListItemDtoFromListItem(itemsList);
        return itemClient.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getRecommendedItems(@RequestHeader(value = CommonConstants.HEADER_USER_ID, required = false) long userId,
                                             @RequestParam(name = "text") String text) {

        log.info("Получен запрос по поиску предметов {}", text);
//        List<Item> itemsList = itemService.getItemsBySearchRequest(text, userId);
//        log.info("Получен список поиска предметов по запросу: {} - {}", text, itemsList);
//        return ItemMapper.toListItemDtoFromListItem(itemsList);
        return itemClient.getRecommendedItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                                        @PathVariable long itemId,
                                        @RequestBody @Valid CommentCreateDto commentCreateDto) {

        log.info("Получен запрос по добавлению комментария к предмету по идентификатору {} " +
                "от пользователя по идентификатору {}", itemId, userId);

//        Comment comment = CommentMapperMapStruct.INSTANCE.toCommentFromCommentCreateDto(commentCreateDto);
//        Comment comment = CommentMapper.toCommentFromCommentCreateDto(commentCreateDto);

//        Comment commentSaved = itemService.createComment(comment, itemId, userId);
//        log.info("Создан комментарий с идентификатором {} по предмету {} от пользователя {} ", commentSaved.getId(), itemId, userId);
//        return CommentMapper.toCommentInfoDtoFromComment(commentSaved);
//        return CommentMapperMapStruct.INSTANCE.toCommentInfoDtoFromComment(commentSaved);
        return itemClient.createComment(userId, itemId, commentCreateDto);
    }




}
