package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConditionsNotRespected;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository{

    Map<Long, List<Item>> usersItems = new HashMap<>();

    private long itemIdCounter = 1;

    public static final String ITEM_NOT_FOUND = "Предмет с id: %d не найден!";


    @Override
    public Item createItem(Item item) {

        long userId = item.getOwner();

        if (!usersItems.containsKey(userId)) {
            item.setId(generateItemId());
            // С использованием List.of(item) - список будет немодифицируемым: НЕЛЬЗЯ удалить/добавить объект
            usersItems.put(userId, new ArrayList<>(List.of(item)));
        }

//        Optional<Item> itemFoundOpt = usersItems.get(userId).stream()
//                .filter(it -> it.getName().equals(item.getName()))
//                .filter(it -> it.getDescription().equals(item.getDescription()))
//                .findFirst();
//
//        if (itemFoundOpt.isPresent()) {
//            throw new DuplicatedDataException("Предмет с таким названием и описанием уже найден");
//        }

        item.setId(generateItemId());
        usersItems.get(userId).add(item);

        return item;
    }

    @Override
    public Item updateItem(Item item) {

        long userId = item.getOwner();
        System.out.println(userId);

        if (!usersItems.containsKey(userId)) {
            throw new NotFoundException("У Пользователя с id: " + userId + " не найдено предметов!");
        }

        Optional<Item> itemFoundOpt = usersItems.values().stream()
                .flatMap(List::stream)
                .filter(it -> it.getId() == item.getId())
                .findFirst();

        if (itemFoundOpt.isEmpty()) {
            throw new NotFoundException(String.format(ITEM_NOT_FOUND, item.getId()));
        }

        Item itemFound = itemFoundOpt.get();

//        if (itemFound.getOwner() != item.getOwner()) {
//            throw new ConditionsNotRespected("Редактировать предмет может только его владелец!");
//        }

        itemFound.setId(item.getId());
        itemFound.setName(item.getName());
        itemFound.setDescription(item.getDescription());
        itemFound.setAvailable(item.getAvailable());
        itemFound.setItemRequest(item.getItemRequest());

        return itemFound;
    }


    @Override
    public Item getItemById(long itemId) {

        Optional<Item> itemFoundOpt = usersItems.values().stream()
                .flatMap(List::stream) // Из списка в поток объектов Item
                .filter(it -> it.getId() == itemId)
                .findFirst();

        if (itemFoundOpt.isEmpty()) {
            throw new NotFoundException(String.format(ITEM_NOT_FOUND, itemId));
        }
        return itemFoundOpt.get();
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {

        if (!usersItems.containsKey(userId)) {
            throw new NotFoundException("У Пользователя с id: " + userId + " не найдено предметов!");
        }

        List<Item> userItems = new ArrayList<>();

         usersItems.get(userId)
                .forEach( it -> {
                    if (!userItems.contains(it)) {
                        userItems.add(it);
                    }
                });

        return userItems;
    }

    @Override
    public List<Item> getItemsBySearchRequest(String text, long userId) {

        if (text.isBlank()) {
            return Collections.emptyList();
        }

        List<Item> resultItemList = new ArrayList<>();

        return usersItems.get(userId).stream()
                .filter(Item::getAvailable)
                .filter(it -> it.getName().equalsIgnoreCase(text) || it.getDescription().equalsIgnoreCase(text))
                .distinct() // Удаление дубликатов
                .toList();
    }

    @Override
    public List<Item> deleteUserById(long userId) {

        if (!usersItems.containsKey(userId)) {
            return Collections.emptyList();
        }

        List<Item> userItems = usersItems.get(userId);
        usersItems.remove(userId);
        return userItems;
    }




    private long generateItemId() {
        return itemIdCounter++;
    }

}
