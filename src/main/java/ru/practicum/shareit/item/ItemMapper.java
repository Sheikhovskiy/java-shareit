package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemCommentInfoDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@UtilityClass
public class ItemMapper {

    public ItemCommentInfoDto toItemCommentInfoDtoFromItem(Item item) {

        ItemCommentInfoDto itemCommentInfoDto = new ItemCommentInfoDto();

        itemCommentInfoDto.setId(item.getId());
        itemCommentInfoDto.setName(item.getName());
        itemCommentInfoDto.setDescription(item.getDescription());
        itemCommentInfoDto.setAvailable(item.getAvailable());
        itemCommentInfoDto.setComments(CommentMapper.toListCommentInfoDtoFromListComment(item.getCommentList()));

        return itemCommentInfoDto;
    }

    public ItemDto toItemDtoFromItem(Item item) {

        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }

    public List<ItemDto> toListItemDtoFromListItem(List<Item> itemList) {

        return itemList.stream()
                .map(ItemMapper::toItemDtoFromItem)
                .toList();
    }

    public Item toItemFromCreatedDto(ItemCreateDto itemCreateDto, User itemUser) {

        Item item = new Item();

        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setAvailable(itemCreateDto.getAvailable());
        item.setOwner(itemUser);

        return item;
    }

    public Item toItemDtoFromItemUpdateDto(ItemUpdateDto itemUpdateDto, User itemUser) {

        Item item = new Item();

        item.setId(itemUpdateDto.getId());
        item.setName(itemUpdateDto.getName());
        item.setDescription(itemUpdateDto.getDescription());
        item.setAvailable(itemUpdateDto.getAvailable());
        item.setOwner(itemUser);

        return item;
    }


}
