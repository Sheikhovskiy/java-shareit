package ru.practicum.shareit.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.ItemRequest;



@Data
@EqualsAndHashCode(of = "id")
public class Item {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private long owner;

    private ItemRequest itemRequest;

}
