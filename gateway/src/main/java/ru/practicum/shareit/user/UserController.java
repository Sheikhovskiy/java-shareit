package ru.practicum.shareit.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;


@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {

        log.info("Получен запрос на создание пользователя {}.", userCreateDto);
        ResponseEntity<Object> user =  userClient.createUser(userCreateDto);
        log.info("Получен ответ на запрос {} ", user);
        return user;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {

        log.info("Получен запрос на поиск пользователя по идентификатору {}.", id);
        ResponseEntity<Object> user =  userClient.getUserById(id);
        log.info("Получен ответ на запрос {} ", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUserById(@PathVariable long userId,
                                    @RequestBody @Valid UserUpdateDto userUpdateDto) {

        log.info("Получен запрос на обновление пользователя по идентификатору {}", userId);
        userUpdateDto.setId(userId);
        return userClient.updateUser(userId, userUpdateDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long userId) {

        log.info("Получен запрос на удаление пользователя по идентификатору {}", userId);
        return userClient.deleteUserById(userId);
    }


}
