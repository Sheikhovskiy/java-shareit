package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<Long, User> users = new HashMap<>();

    private long userIdCounter = 1;

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
    public User updateUserById(User user) {

        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с почтой " +  user.getId() + " не найден!");
        }

        Optional<User> userEmailOpt = users.values().stream()
                .filter(us -> us.getEmail() != null && us.getEmail().equals(user.getEmail()))
                .findFirst();

        if (userEmailOpt.isPresent()) {
            throw new DuplicatedDataException("Такая почта уже привязана к одному из аккаунтов пользователей!");
        }

        User savedUser = users.get(user.getId());

        if (user.getName() != null && !user.getName().isBlank()) {
            savedUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            savedUser.setEmail(user.getEmail());
        }

        return user;
    }

    @Override
    public User deleteUserById(long userId) {

        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
        }
        User user = users.get(userId);
        return users.remove(userId);
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
