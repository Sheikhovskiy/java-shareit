package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    Map<Long, User> users = new HashMap<>();

    private long userIdCounter = 0;

    private static final String USER_NOT_FOUND = "Пользователь с идентификатором {} не найден!";

    @Override
    public User createUser(User user) {

        Optional<User> userOpt = users.values().stream()
                .filter(us -> us.getEmail().equals(user.getEmail()))
                .findFirst();

        if (userOpt.isPresent()) {
            throw new DuplicatedDataException(String.format("Пользователь с электронной почтой: {} уже существует!", user.getEmail()));
        }
        user.setId(generateUserId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long userId) {

        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
        }
        return users.get(userId);
    }

    @Override
    public User updateUserById(UserUpdateDto userUpdateDto) {

        if (!users.containsKey(userUpdateDto.getId())) {
            throw new NotFoundException("Пользователь с почтой " +  userUpdateDto.getId() + " не найден!");
        }

        Optional<User> userEmailOpt = users.values().stream()
                        .filter(us -> us.getEmail() != null && us.getEmail().equals(userUpdateDto.getEmail()))
                        .findFirst();

        if (userEmailOpt.isPresent()) {
            throw new DuplicatedDataException("Такая почта уже привязана к одному из аккаунтов пользователей!");
        }

        User user = new User();

        user.setId(userUpdateDto.getId());
        user.setName(userUpdateDto.getName());
        user.setEmail(userUpdateDto.getEmail());
        return user;
    }

    @Override
    public User deleteUserById(long userId) {

        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
        }
        User user = users.get(userId);
        users.remove(userId);
        return user;
    }

    @Override
    public boolean userDoesExist(long userId) {

        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
        }
        return true;
    }




    public long generateUserId() {
        return userIdCounter++;
    }
}
