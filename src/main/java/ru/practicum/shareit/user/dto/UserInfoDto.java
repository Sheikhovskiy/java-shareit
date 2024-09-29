package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.item.Item;

import java.util.List;

@Data
public class UserInfoDto {

    private String name;

    private String email;

    private List<Item> userItems;
}
