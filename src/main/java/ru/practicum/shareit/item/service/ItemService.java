package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemIdDto createItem(ItemCreateDto itemCreateDto);

    ItemDto updateItem(ItemUpdateDto itemUpdateDto);

    ItemDto getItemInfoById(long itemId);

    List<ItemDto> getAllItemsByUserId(long userId);

    List<ItemDto> getItemsBySearchRequest(String text);

}
