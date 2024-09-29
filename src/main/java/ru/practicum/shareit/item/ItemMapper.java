package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDtoFromItem(Item item) {

        ItemDto itemDto = new ItemDto();

        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequest(item.getItemRequest() != null ? item.getItemRequest() : null);

        return itemDto;
    }

    public static ItemIdDto toItemIdDtoFromItem(Item item) {

        ItemIdDto itemIdDto = new ItemIdDto();

        itemIdDto.setId(item.getId());
        itemIdDto.setName(item.getName());
        itemIdDto.setDescription(item.getDescription());
        itemIdDto.setAvailable(item.getAvailable());
        itemIdDto.setRequest(item.getItemRequest());

        return itemIdDto;
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
        item.setItemRequest(itemUpdateDto.getRequest());

        return item;
    }


}
