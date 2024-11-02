package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
public class RequestInfoDto {

    private long id;

    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;

}