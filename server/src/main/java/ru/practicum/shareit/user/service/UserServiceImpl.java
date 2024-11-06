package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long userId) {

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format("Пользователя с идентификатором {} не существует!", userId));
        }
        return userOpt.get();
    }

    @Override
    public User updateUserById(User user) {

        return userRepository.save(user);
    }

    @Override
    public User deleteUserById(long userId) {

        return userRepository.deleteById(userId);
    }

    @Override
    public boolean existsById(long userId) {
        return userRepository.existsById(userId);
    }


}
