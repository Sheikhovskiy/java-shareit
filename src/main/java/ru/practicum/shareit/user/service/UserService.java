package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto createUser(UserCreateDto userCreateDto);

}
