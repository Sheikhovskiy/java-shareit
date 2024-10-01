package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@UtilityClass
public class ItemMapper {

    public static ItemDto toItemDtoFromItem(Item item) {

        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }

    public static List<ItemDto> toListItemDtoFromListItem(List<Item> itemList) {

        return itemList.stream()
                .map(ItemMapper::toItemDtoFromItem)
                .toList();
    }

    public static Item toItemFromCreatedDto(ItemCreateDto itemCreateDto) {

        Item item = new Item();

        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setAvailable(itemCreateDto.getAvailable());
        item.setOwner(itemCreateDto.getOwner());

        return item;
    }

    public static Item toItemDtoFromItemUpdateDto(ItemUpdateDto itemUpdateDto) {

        Item item = new Item();

        item.setId(itemUpdateDto.getId());
        item.setName(itemUpdateDto.getName());
        item.setDescription(itemUpdateDto.getDescription());
        item.setAvailable(itemUpdateDto.getAvailable());
        item.setOwner(itemUpdateDto.getOwner());

        return item;
    }


}
