package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemService {

    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemInfoById(long itemId);

    List<Item> getAllItemsByUserId(long userId);

    List<Item> getItemsBySearchRequest(String text, long userId);

    Comment createComment(Comment comment, long itemId, long userId);

}
