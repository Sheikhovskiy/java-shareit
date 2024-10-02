package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.*;


@UtilityClass
public class UserMapper {

    public UserDto toUserDtoFromUser(User user) {

       UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public UserInfoDto toUserInfoDtoFromUser(User user) {

        UserInfoDto userInfoDto = new UserInfoDto();

        userInfoDto.setName(user.getName());
        userInfoDto.setEmail(user.getEmail());

        return userInfoDto;
    }

    public User toUserFromUserCreateDto(UserCreateDto userCreateDto) {
        User user = new User();

        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());

        return user;
    }

    public User toUserFromUserUpdateDto(UserUpdateDto userUpdateDto) {
        User user = new User();

        user.setId(userUpdateDto.getId());
        user.setName(userUpdateDto.getName());
        user.setEmail(userUpdateDto.getEmail());

        return user;
    }

}
