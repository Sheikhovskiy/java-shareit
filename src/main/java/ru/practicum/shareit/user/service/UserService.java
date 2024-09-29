package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserIdDto;
import ru.practicum.shareit.user.dto.UserInfoDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserIdDto createUser(UserCreateDto userCreateDto);

    UserIdDto getUserById(long userId);

    UserIdDto updateUserById(UserUpdateDto userUpdateDto);

    UserInfoDto deleteUserById(long userId);
}
