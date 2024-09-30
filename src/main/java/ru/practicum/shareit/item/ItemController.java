package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 *
 *
 *  PUT - обновление объекта целиком, PATCH - обновление поля объекта, можно и методом PUT обновить одно поле,
 *  однако метод PUT будет проходить все поля объекта и искать необходимое, в отличии от PATCH, который не обходит объект целиком
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemService itemService;


    @PostMapping
    public ItemIdDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestBody @Valid ItemCreateDto itemCreateDto) {

        itemCreateDto.setOwner(userId);
        log.info("Получен предмет на создание " + itemCreateDto);
        ItemIdDto itemIdDto = itemService.createItem(itemCreateDto);
        log.info("Предмет создан " + itemIdDto);
        return itemIdDto;
    }

    @PatchMapping("/{itemId}")
    public ItemIdDto updateItem(@PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody @Valid ItemUpdateDto itemUpdateDto) {
        itemUpdateDto.setOwner(userId);
        itemUpdateDto.setId(itemId);
        log.info("Получен предмет на обновление " + itemUpdateDto);
        ItemIdDto itemIdDto = itemService.updateItem(itemUpdateDto);
        log.info("Объект обновлён " + itemIdDto);
        return itemIdDto;
    }

    @GetMapping("/{itemId}")
    public ItemIdDto getItemInfoById(@PathVariable long itemId) {

        log.info("Получен идентификатор предмета {}", itemId);
        ItemIdDto itemIdDto = itemService.getItemInfoById(itemId);
        log.info("Получен предмет с идентификатором {} - {}", itemId, itemIdDto);
        return itemIdDto;
    }

    @GetMapping
    public List<ItemIdDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Получен идентификатор пользователя {} для получения всех фильмов пользователя", userId);
        List<ItemIdDto> itemIdDtoList = itemService.getAllItemsByUserId(userId);
        log.info("Получен список всех предметов пользователя с идентификатором {} - {}", userId, itemIdDtoList);
        return itemIdDtoList;
    }

    @GetMapping("/search")
    public List<ItemDto> getRecommendedItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) long userId,
                                             @RequestParam String text) {


        log.info("Получен запрос по поиску предметов {}", text);
        List<ItemDto> itemDtoList = itemService.getItemsBySearchRequest(text, userId);
        log.info("Получен список поиска предметов по запросу: {} - {}", text, itemDtoList);
        return itemDtoList;
    }


}
