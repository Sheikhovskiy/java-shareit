package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserUpdateDto;


public interface UserRepository {

    User createUser(User user);

    User getUserById(long userId);

    User updateUserById(UserUpdateDto userUpdateDto);

    User deleteUserById(long userId);

    boolean userDoesExist(long userId);
}
