package ru.practicum.shareit.user;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserIdDto;
import ru.practicum.shareit.user.dto.UserInfoDto;

import java.util.List;

public class UserMapper {

    public static UserDto toUserDtoFromUser(User user) {

       UserDto userDto = new UserDto();

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public static UserIdDto toUserIdDtoFromUser(User user) {

        UserIdDto userIdDto = new UserIdDto();

        userIdDto.setId(user.getId());
        userIdDto.setName(user.getName());
        userIdDto.setEmail(user.getEmail());

        return userIdDto;
    }

    public static UserInfoDto toUserInfoDtoFromUser(User user, List<Item> userItems) {

        UserInfoDto userInfoDto = new UserInfoDto();

        userInfoDto.setName(user.getName());
        userInfoDto.setEmail(user.getEmail());
        userInfoDto.setUserItems(userItems);

        return userInfoDto;
    }

    public static User toUserFromUserDto(UserDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }

    public static User toUserFromUserCreateDto(UserCreateDto userCreateDto) {
        User user = new User();

        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());

        return user;
    }

}
