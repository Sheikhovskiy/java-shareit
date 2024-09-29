package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserIdDto;
import ru.practicum.shareit.user.dto.UserInfoDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserIdDto createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("Получен запрос на создание пользователя {}.", userCreateDto);
        UserIdDto userIdDto = userService.createUser(userCreateDto);
        log.info("Пользователь {} успешно создан!", userIdDto);
        return userIdDto;
    }

    @GetMapping("/{userId}")
    public UserIdDto getUserById(@PathVariable long userId) {
        log.info("Получен запрос на поиск пользователя по идентификатору {}.", userId);
        UserIdDto userIdDto = userService.getUserById(userId);
        log.info("Получен пользователь с идентификатором {}", userId);
        return userIdDto;
    }

    @PatchMapping("/{userId}")
    public UserIdDto updateUserById(@PathVariable long userId,
                                    @RequestBody @Valid UserUpdateDto userUpdateDto) {
        userUpdateDto.setId(userId);
        log.info("Получен запрос на обновление пользователя по идентификатору {}", userId);
        UserIdDto userIdDto = userService.updateUserById(userUpdateDto);
        log.info("Пользователь с идентификатором {} успешно обновлён", userIdDto.getId());
        return userIdDto;
    }



    @DeleteMapping("/{userId}")
    public UserInfoDto deleteUserById(@PathVariable long userId) {

        log.info("Получен запрос на удаление пользователя по идентификатору {}", userId);
        UserInfoDto userInfoDto = userService.deleteUserById(userId);
        log.info("Пользователь с идентификатором {} - {}", userId, userInfoDto);
        return userInfoDto;
    }


}
