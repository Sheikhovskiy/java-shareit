package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

@Data
@EqualsAndHashCode(of = "id")
public class Item {

    private long id;

    private String name;

    private String description;

    private Boolean isAvailable;

    private long owner;

    private ItemRequest itemRequest;

}
