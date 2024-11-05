package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;


public interface UserService {

    User createUser(User user);

    User getUserById(long userId);

    User updateUserById(User user);

    User deleteUserById(long userId);

//    List<User> getAllUsers();
}
