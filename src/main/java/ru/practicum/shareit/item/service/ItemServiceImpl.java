package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public Item createItem(Item item) {

        userRepository.userDoesExist(item.getOwner());

        return itemRepository.createItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        return itemRepository.updateItem(item);
    }

    @Override
    public Item getItemInfoById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return itemRepository.getAllItemsByUserId(userId);
    }

    @Override
    public List<Item> getItemsBySearchRequest(String text, long userId) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.getItemsBySearchRequest(text, userId);
    }



}
