package ru.practicum.shareit.item.repository;


import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemRepository {

    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemById(long itemId);

    List<Item> getAllItemsByUserId(long userId);

    List<Item> getItemsBySearchRequest(String text);

}
