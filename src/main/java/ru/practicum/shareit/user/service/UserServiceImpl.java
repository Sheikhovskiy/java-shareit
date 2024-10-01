package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

//    private final ItemRepository itemRepository;

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public User updateUserById(User user) {
        String userUpdatedName = user.getName();

        return userRepository.updateUserById(user);
    }

    @Override
    public User deleteUserById(long userId) {
        return userRepository.deleteUserById(userId);
//        List<Item> userItems = itemRepository.deleteUserById(userId);

    }
}
