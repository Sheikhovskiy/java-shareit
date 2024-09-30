package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserIdDto;
import ru.practicum.shareit.user.dto.UserInfoDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    public UserIdDto createUser(UserCreateDto userCreateDto) {
        User user = userRepository.createUser(UserMapper.toUserFromUserCreateDto(userCreateDto));

        System.out.println(UserMapper.toUserIdDtoFromUser(user));
        return UserMapper.toUserIdDtoFromUser(user);
    }

    @Override
    public UserIdDto getUserById(long userId) {
        User user = userRepository.getUserById(userId);

        return UserMapper.toUserIdDtoFromUser(user);
    }

    @Override
    public UserIdDto updateUserById(UserUpdateDto userUpdateDto) {
        User user = userRepository.updateUserById(userUpdateDto);

        return UserMapper.toUserIdDtoFromUser(user);
    }

    @Override
    public UserInfoDto deleteUserById(long userId) {
        User user = userRepository.deleteUserById(userId);

        List<Item> userItems = itemRepository.deleteUserById(userId);

        return UserMapper.toUserInfoDtoFromUser(user, userItems);
    }
}
