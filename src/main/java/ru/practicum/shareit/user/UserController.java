package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.service.UserService;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserCreateDto userCreateDto) {

        log.info("Получен запрос на создание пользователя {}.", userCreateDto);
        User user = userService.createUser(UserMapper.toUserFromUserCreateDto(userCreateDto));
        log.info("Пользователь {} успешно создан!", user);
        return UserMapper.toUserDtoFromUser(user);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {

        log.info("Получен запрос на поиск пользователя по идентификатору {}.", userId);
        User user = userService.getUserById(userId);
        log.info("Получен пользователь с идентификатором {}", userId);
        return UserMapper.toUserDtoFromUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUserById(@PathVariable long userId,
                                    @RequestBody @Valid UserUpdateDto userUpdateDto) {

        userUpdateDto.setId(userId);
        log.info("Получен запрос на обновление пользователя по идентификатору {}", userId);
        User user = userService.updateUserById(UserMapper.toUserFromUserUpdateDto(userUpdateDto));
        log.info("Пользователь с идентификатором {} успешно обновлён", user.getId());
        return UserMapper.toUserDtoFromUser(user);
    }

    @DeleteMapping("/{userId}")
    public UserInfoDto deleteUserById(@PathVariable long userId) {

        log.info("Получен запрос на удаление пользователя по идентификатору {}", userId);
        User user = userService.getUserById(userId);
        userService.deleteUserById(userId);
        log.info("Пользователь с идентификатором {} - {}", userId, user);
        return UserMapper.toUserInfoDtoFromUser(user);
    }


}
