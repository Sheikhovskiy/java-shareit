package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;


public interface UserRepositoryOld {

    User createUser(User user);

    User getUserById(long userId);

    User updateUserById(User user);

    User deleteUserById(long userId);

    boolean userDoesExist(long userId);
}
