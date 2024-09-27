package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;

public interface UserRepository {

    User createUser(UserCreateDto userCreateDto);

}
