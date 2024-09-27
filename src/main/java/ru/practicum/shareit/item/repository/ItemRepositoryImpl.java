package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConditionsNotRespected;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        Optional<Item> itemFoundOpt = usersItems.get(userId).stream()
                .filter(it -> it.getName().equals(item.getName()))
                .filter(it -> it.getDescription().equals(item.getDescription()))
                .findFirst();

        if (itemFoundOpt.isPresent()) {
            throw new DuplicatedDataException("Предмет с таким названием и описанием уже найден");
        }

        item.setId(generateItemId());
        usersItems.get(userId).add(item);

        return item;
    }

    @Override
    public Item updateItem(Item item) {

        long userId = item.getOwner();

        if (!usersItems.containsKey(userId)) {
            throw new NotFoundException("У пользователя с id: " + item.getOwner() + " нет предметов!");
        }

        Optional<Item> itemFoundOpt = usersItems.get(userId).stream()
                .filter(it -> it.getId() == item.getId())
                .findFirst();

        if (itemFoundOpt.isEmpty()) {
            throw new NotFoundException(String.format(ITEM_NOT_FOUND, item.getId()));
        }

        Item itemFound = itemFoundOpt.get();

        if (itemFound.getOwner() != item.getOwner()) {
            throw new ConditionsNotRespected("Редактировать предмет может только его владелец!");
        }

        itemFound.setName(item.getName());
        itemFound.setDescription(item.getDescription());
        itemFound.setIsAvailable(item.getIsAvailable());
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
            throw new NotFoundException("Пользователь с id: " + userId + " не найден!");
        }

        return usersItems.get(userId).stream()
                .toList();
    }





    private long generateItemId() {
        return itemIdCounter++;
    }

}
