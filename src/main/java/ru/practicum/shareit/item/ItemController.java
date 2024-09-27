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
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

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
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @RequestBody @Valid ItemCreateDto itemCreateDto) {

        itemCreateDto.setUserId(userId);
        log.info("Получен предмет на создание " + itemCreateDto);
        ItemDto itemDto = itemService.createItem(itemCreateDto);
        log.info("Предмет создан " + itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                                  @RequestBody @Valid ItemUpdateDto itemUpdateDto) {
        itemUpdateDto.setId(itemId);
        log.info("Получен предмет на обновление " + itemUpdateDto);
        ItemDto itemDto = itemService.updateItem(itemUpdateDto);
        log.info("Объект обновлён " + itemDto);
        return itemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemInfoById(@PathVariable long itemId) {

        log.info("Получен идентификатор предмета {}", itemId);
        ItemDto itemDto = itemService.getItemInfoById(itemId);
        log.info("Получен предмет с идентификатором {} - {}", itemId, itemDto);
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Получен идентификатор пользователя {} для получения всех фильмов пользователя", userId);
        List<ItemDto> itemDtoList = itemService.getAllItemsByUserId(userId);
        log.info("Получен список всех предметов пользователя с идентификатором {} - {}", userId, itemDtoList);
        return itemDtoList;
    }

    @GetMapping("/search")
    public List<ItemDto> getRecommendedItems(@RequestParam String text) {
        return null;
    }


}
