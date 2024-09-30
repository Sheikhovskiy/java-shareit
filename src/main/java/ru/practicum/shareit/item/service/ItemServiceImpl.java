package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public ItemIdDto createItem(ItemCreateDto itemCreateDto) {

        userRepository.userDoesExist(itemCreateDto.getOwner());

        Item item = itemRepository.createItem(ItemMapper.toItemFromCreatedDto(itemCreateDto));
        System.out.println("ITEM ----->" + ItemMapper.toItemIdDtoFromItem(item));
        return ItemMapper.toItemIdDtoFromItem(item);
    }

    @Override
    public ItemIdDto updateItem(ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.updateItem(toItemDtoFromItemUpdateDto(itemUpdateDto));

        return ItemMapper.toItemIdDtoFromItem(item);
    }

    @Override
    public ItemIdDto getItemInfoById(long itemId) {
        Item item = itemRepository.getItemById(itemId);

        return ItemMapper.toItemIdDtoFromItem(item);
    }

    @Override
    public List<ItemIdDto> getAllItemsByUserId(long userId) {
        List<Item> userItems = itemRepository.getAllItemsByUserId(userId);

        return ItemMapper.toListItemIdDtoFromListItem(userItems);
    }

    @Override
    public List<ItemDto> getItemsBySearchRequest(String text) {
        List<Item> foundItems = itemRepository.getItemsBySearchRequest(text);

        return ItemMapper.toListItemDtoFromListItem(foundItems);
    }


}
