package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


//   С использованием List.of(item) - список будет немодифицируемым: НЕЛЬЗЯ удалить/добавить объект
//   usersItems.put(userId, new ArrayList<>(List.of(item)));

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private Map<Long, List<Item>> usersItems = new HashMap<>();

    private Map<Long, Item> items = new HashMap<>();

    private long itemIdCounter = 1;

    public static final String ITEM_NOT_FOUND = "Предмет с id: %d не найден!";


    @Override
    public Item createItem(Item item) {

        final List<Item> itemsList = usersItems.computeIfAbsent(item.getOwner(), k -> new ArrayList<>());

        item.setId(generateItemId());
        itemsList.add(item);
        items.put(item.getId(), item);

        return item;
    }

    @Override
    public Item updateItem(Item item) {

        long userId = item.getOwner();

        if (!usersItems.containsKey(userId)) {
            throw new NotFoundException("У Пользователя с id: " + userId + " не найдено предметов!");
        }

        Optional<Item> itemSavedOpt = Optional.ofNullable(items.get(item.getId()));

        if (itemSavedOpt.isEmpty()) {
            throw new NotFoundException(String.format(ITEM_NOT_FOUND, item.getId()));
        }

        Item itemSaved = items.get(item.getId());

        if (item.getName() != null && !item.getName().isBlank()) {
            itemSaved.setName(item.getName());
        }

        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemSaved.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            itemSaved.setAvailable(item.getAvailable());
        }

        if (item.getItemRequest() != null) {
            itemSaved.setItemRequest(item.getItemRequest());
        }

        return itemSaved;
    }

    @Override
    public Item getItemById(long itemId) {

        Optional<Item> itemFoundOpt = Optional.ofNullable(items.get(itemId));

        if (itemFoundOpt.isEmpty()) {
            throw new NotFoundException(String.format(ITEM_NOT_FOUND, itemId));
        }

        return itemFoundOpt.get();
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {

        if (!usersItems.containsKey(userId)) {
            return Collections.emptyList();
        }

        List<Item> userItems = new ArrayList<>();

         usersItems.get(userId)
                .forEach(it -> {
                    if (!userItems.contains(it)) {
                        userItems.add(it);
                    }
                });

        return userItems;
    }

    @Override
    public List<Item> getItemsBySearchRequest(String text, long userId) {

        return usersItems.get(userId).stream()
                .filter(Item::getAvailable)
                .filter(it -> it.getName().equalsIgnoreCase(text) || it.getDescription().equalsIgnoreCase(text))
                .distinct() // Удаление дубликатов
                .toList();
    }

    @Override
    public List<Item> deleteByUserId(long userId) {

        if (!usersItems.containsKey(userId)) {
            return Collections.emptyList();
        }

        List<Item> userItems = usersItems.get(userId);
        return usersItems.remove(userId);
    }


    private long generateItemId() {
        return itemIdCounter++;
    }

}
