package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemCreateDto itemCreateDto);

    ItemDto updateItem(ItemUpdateDto itemUpdateDto);

    ItemDto getItemInfoById(long itemId);

    List<ItemDto> getAllItemsByUserId(long userId);

}
