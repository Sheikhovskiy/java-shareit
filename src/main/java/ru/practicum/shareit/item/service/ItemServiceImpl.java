package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    @Override
    public ItemDto createItem(ItemCreateDto itemCreateDto) {
        Item item = itemRepository.createItem(ItemMapper.toItemFromCreatedDto(itemCreateDto));

        return toItemDtoFromItem(item);
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.updateItem(toItemDtoFromItemUpdateDto(itemUpdateDto));

        return ItemMapper.toItemDtoFromItem(item);
    }

    @Override
    public ItemDto getItemInfoById(long itemId) {
        Item item = itemRepository.getItemById(itemId);

        return ItemMapper.toItemDtoFromItem(item);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(long userId) {
        List<Item> userItems = itemRepository.getAllItemsByUserId(userId);

        return ItemMapper.toListItemDtoFromListItem(userItems);


    }


}
