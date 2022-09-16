package ru.practicum.shareit.item;

import jakarta.validation.Valid;
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
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

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

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";


    @PostMapping
    public ItemDto createItem(@RequestHeader(HEADER_USER_ID) long userId,
                                @RequestBody @Valid ItemCreateDto itemCreateDto) {

        itemCreateDto.setOwner(userId);
        log.info("Получен предмет на создание {}", itemCreateDto);
        Item item = itemService.createItem(ItemMapper.toItemFromCreatedDto(itemCreateDto));
        log.info("Предмет создан {}", item);
        return ItemMapper.toItemDtoFromItem(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestHeader(HEADER_USER_ID) long userId,
                              @RequestBody @Valid ItemUpdateDto itemUpdateDto) {

        itemUpdateDto.setOwner(userId);
        itemUpdateDto.setId(itemId);
        log.info("Получен предмет на обновление {}", itemUpdateDto);
        Item item = itemService.updateItem(ItemMapper.toItemDtoFromItemUpdateDto(itemUpdateDto));
        log.info("Объект обновлён {}", item);
        return ItemMapper.toItemDtoFromItem(item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemInfoById(@PathVariable long itemId) {

        log.info("Получен идентификатор предмета {}", itemId);
        Item item = itemService.getItemInfoById(itemId);
        log.info("Получен предмет с идентификатором {} - {}", itemId, item);
        return ItemMapper.toItemDtoFromItem(item);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader(HEADER_USER_ID) long userId) {

        log.info("Получен идентификатор пользователя {} для получения всех фильмов пользователя", userId);
        List<Item> itemsList = itemService.getAllItemsByUserId(userId);
        log.info("Получен список всех предметов пользователя с идентификатором {} - {}", userId, itemsList);
        return ItemMapper.toListItemDtoFromListItem(itemsList);
    }

    @GetMapping("/search")
    public List<ItemDto> getRecommendedItems(@RequestHeader(value = HEADER_USER_ID, required = false) long userId,
                                             @RequestParam String text) {

        log.info("Получен запрос по поиску предметов {}", text);
        List<Item> itemsList = itemService.getItemsBySearchRequest(text, userId);
        log.info("Получен список поиска предметов по запросу: {} - {}", text, itemsList);
        return ItemMapper.toListItemDtoFromListItem(itemsList);
    }


}
