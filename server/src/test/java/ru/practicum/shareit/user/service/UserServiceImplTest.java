package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserServiceImplTest {


    private final EntityManager em;

    private final UserService userService;

    private User userTest;

    @BeforeEach
    void init() throws Exception {
        userTest = new User();

        userTest.setId(1L);
        userTest.setName("userTestName");
        userTest.setEmail("userTestEmail");
    }


    @Test
    @DisplayName("Создание пользователя")
    void testCreateUser() throws Exception {

        User userCreateTest = new User();

        userCreateTest.setName("userTestName");
        userCreateTest.setEmail("userTestEmail");

        User userReceived = userService.createUser(userCreateTest);
        UserDto userDtoReceived = makeUserDto(userReceived);

        UserDto userDtoExpected = makeUserDto(userTest);

        assertThat(userReceived, allOf(
                hasProperty("id", equalTo(userDtoExpected.getId())),
                hasProperty("name", equalTo(userDtoExpected.getName())),
                hasProperty("email", equalTo(userDtoExpected.getEmail()))
        ));
    }

    @Test
    @DisplayName("Обновить пользователя по id")
    void testUpdateUserById() throws Exception {

        User userUpdateTest = new User();

        userUpdateTest.setId(11L);
        userUpdateTest.setName("userTestUpdatedName");
        userUpdateTest.setEmail("userTestEmail");

        User userReceived = userService.updateUserById(userUpdateTest);

        UserDto userDtoReceived = makeUserDto(userReceived);
        UserDto userDtoExpected = makeUserDto(userUpdateTest);


        assertThat(userDtoReceived, allOf(
                hasProperty("id", equalTo(userDtoExpected.getId())),
                hasProperty("name", equalTo(userDtoExpected.getName())),
                hasProperty("email", equalTo(userDtoExpected.getEmail()))
        ));
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void testGetUserById() throws Exception {

        long userTestId = 11L;

        User userReceived = userService.getUserById(11L);
        UserDto userDtoReceived = makeUserDto(userReceived);

        assertThat(userDtoReceived, allOf(
                hasProperty("id", equalTo(11L)),
                hasProperty("name", equalTo("testFirstUser")),
                hasProperty("email", equalTo("testFirstEmail"))
        ));
    }

    @Test
    @DisplayName("Удаление пользователя по id")
    void testDeleteUserById() throws Exception {

        long userTestId = 11L;

        User userReceived = userService.deleteUserById(userTestId);
        System.out.println(userReceived);

        assertThat(userReceived, nullValue());
//        UserDto userDtoReceived = makeUserDto(userReceived);

//        User userReceivedDeleted = userService.getUserById(userTestId);
//        UserDto userDtoReceivedDeleted = makeUserDto(userReceivedDeleted);

//        assertEquals(false, userService.existsById(userTestId));
    }




    private UserDto makeUserDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }


}