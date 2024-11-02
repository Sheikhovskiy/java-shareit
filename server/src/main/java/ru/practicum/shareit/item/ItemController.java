package ru.practicum.shareit.item;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.item.dto.ItemCommentInfoDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ru.practicum.shareit.item.dto.CommentInfoDto;

/**
 *  PUT - обновление объекта целиком, PATCH - обновление поля объекта, можно и методом PUT обновить одно поле,
 *  однако метод PUT будет проходить все поля объекта и искать необходимое, в отличии от PATCH, который не обходит объект целиком
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    private final UserService userService;

    private final RequestService requestService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                                @RequestBody ItemCreateDto itemCreateDto) {

        itemCreateDto.setOwner(userId);
        log.info("Получен предмет на создание {}", itemCreateDto);
        User itemOwner = userService.getUserById(userId);

        Item item;
        if (itemCreateDto.getRequestId() != 0) {
            Request request = requestService.getRequestById(itemCreateDto.getRequestId());
            item = itemService.createItem(ItemMapper.toItemFromCreatedDtoWithRequest(itemCreateDto, itemOwner, request));
        } else {
            item = itemService.createItem(ItemMapper.toItemFromCreatedDto(itemCreateDto, itemOwner));
        }
        log.info("Предмет создан {}", item);
        return ItemMapper.toItemDtoFromItem(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                              @RequestBody ItemUpdateDto itemUpdateDto) {

        itemUpdateDto.setOwner(userId);
        itemUpdateDto.setId(itemId);
        log.info("Получен предмет на обновление {}", itemUpdateDto);
        User itemOwner = userService.getUserById(userId);
        Item item = itemService.updateItem(ItemMapper.toItemDtoFromItemUpdateDto(itemUpdateDto, itemOwner));
        log.info("Объект обновлён {}", item);
        return ItemMapper.toItemDtoFromItem(item);
    }

    @GetMapping("/{itemId}")
    public ItemCommentInfoDto getItemInfoById(@PathVariable long itemId) {

        log.info("Получен идентификатор предмета {}", itemId);
        Item item = itemService.getItemInfoById(itemId);
        log.info("Получен предмет с идентификатором {} - {}", itemId, item);
        return ItemMapper.toItemCommentInfoDtoFromItem(item);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId) {

        log.info("Получен идентификатор пользователя {} для получения всех фильмов пользователя", userId);
        List<Item> itemsList = itemService.getAllItemsByUserId(userId);
        log.info("Получен список всех предметов пользователя с идентификатором {} - {}", userId, itemsList);
        return ItemMapper.toListItemDtoFromListItem(itemsList);
    }

    @GetMapping("/search")
    public List<ItemDto> getRecommendedItems(@RequestHeader(value = CommonConstants.HEADER_USER_ID, required = false) long userId,
                                             @RequestParam(name = "text") String text) {

        log.info("Получен запрос по поиску предметов {}", text);
        List<Item> itemsList = itemService.getItemsBySearchRequest(text, userId);
        log.info("Получен список поиска предметов по запросу: {} - {}", text, itemsList);
        return ItemMapper.toListItemDtoFromListItem(itemsList);
    }

    @PostMapping("/{itemId}/comment")
    public CommentInfoDto createComment(@RequestHeader(CommonConstants.HEADER_USER_ID) long userId,
                                        @PathVariable long itemId,
                                        @RequestBody CommentCreateDto commentCreateDto) {

        log.info("Получен запрос по добавлению комментария к предмету по идентификатору {} " +
                "от пользователя по идентификатору {}", itemId, userId);

//        Comment comment = CommentMapperMapStruct.INSTANCE.toCommentFromCommentCreateDto(commentCreateDto);
        Comment comment = CommentMapper.toCommentFromCommentCreateDto(commentCreateDto);

        Comment commentSaved = itemService.createComment(comment, itemId, userId);
        log.info("Создан комментарий с идентификатором {} по предмету {} от пользователя {} ", commentSaved.getId(), itemId, userId);
        return CommentMapper.toCommentInfoDtoFromComment(commentSaved);
//         return CommentMapperMapStruct.INSTANCE.toCommentInfoDtoFromComment(commentSaved);
    }



}
