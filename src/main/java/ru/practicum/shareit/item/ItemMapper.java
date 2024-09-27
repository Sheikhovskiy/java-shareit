package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDtoFromItem(Item item) {

        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setIsAvailable(item.getIsAvailable());
        itemDto.setRequest(item.getItemRequest() != null ? item.getItemRequest() : null);

        return itemDto;
    }

    public static Item toItemFromCreatedDto(ItemCreateDto itemCreateDto) {

        Item item = new Item();

        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setIsAvailable(itemCreateDto.getAvailable());
        item.setOwner(itemCreateDto.getUserId());

        return item;
    }

    public static Item toItemDtoFromItemUpdateDto(ItemUpdateDto itemUpdateDto) {

        Item item = new Item();

        item.setId(itemUpdateDto.getId());
        item.setName(itemUpdateDto.getName());
        item.setDescription(itemUpdateDto.getDescription());
        item.setIsAvailable(itemUpdateDto.getIsAvailable());
        item.setOwner(itemUpdateDto.getOwner());
        item.setItemRequest(itemUpdateDto.getRequest());

        return item;
    }

    public static List<ItemDto> toListItemDtoFromListItem(List<Item> itemList) {

        return itemList.stream()
                .map(ItemMapper::toItemDtoFromItem)
                .toList();
    }



}
