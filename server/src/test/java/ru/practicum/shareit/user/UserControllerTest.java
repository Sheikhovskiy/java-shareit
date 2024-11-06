package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ConstraintConstantsTest.HEADER_USER_ID;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String userDefaultResponse = "{\"id\":1,\"email\":\"userTest@Email\",\"name\":\"userTestName\"}";

    private final String userDefaultDeleteResponse = "{\"email\":\"userTest@Email\",\"name\":\"userTestName\"}";

    private User userTest;

    @BeforeEach
    void init() {
        userTest = new User();

        long userTestId = 1L;
        String userTestName = "userTestName";
        String userTestEmail = "userTest@Email";

        userTest.setId(userTestId);
        userTest.setName(userTestName);
        userTest.setEmail(userTestEmail);
    }

    @Test
    @DisplayName("Создание пользователя")
    void testCreateUser() throws Exception {

        long userTestId = 1L;
        String userCreateDtoTestName = "userTestName";
        String userCreateDtoTestDescription = "userTest@Email";

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName(userCreateDtoTestName);
        userCreateDto.setEmail(userCreateDtoTestDescription);

        String userCreateDtoJson = objectMapper.writeValueAsString(userCreateDto);

        when(userService.createUser(Mockito.any(User.class)))
                .thenReturn(userTest);


        mockMvc.perform(post("/users")
                        .header(HEADER_USER_ID, userTestId)
                        .contentType("application/json")
                        .content(userCreateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userDefaultResponse));

        Mockito.verify(userService, times(1)).createUser(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Обновление пользователя")
    void testUpdateUserById() throws Exception {

        long userTestId = 1L;
        String userCreateDtoTestName = "userTestName";
        String userCreateDtoTestDescription = "userTest@Email";

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName(userCreateDtoTestName);
        userUpdateDto.setEmail(userCreateDtoTestDescription);

        String userUpdateDtoJson = objectMapper.writeValueAsString(userUpdateDto);

        String path = "/users/" + userTestId;

        when(userService.updateUserById(Mockito.any(User.class)))
                .thenReturn(userTest);

        mockMvc.perform(patch(path)
                        .header(HEADER_USER_ID, userTestId)
                        .contentType("application/json")
                        .content(userUpdateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userDefaultResponse));

        Mockito.verify(userService, times(1)).updateUserById(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void testGetUserById() throws Exception {

        long userTestId = 1L;

        String path = "/users/" + userTestId;

        when(userService.getUserById(Mockito.anyLong()))
                .thenReturn(userTest);

        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(content().json(userDefaultResponse));

        Mockito.verify(userService, times(1)).getUserById(userTestId);
    }

    @Test
    @DisplayName("Удаление пользователя по id")
    void testDeleteUserById() throws Exception {

        long userTestId = 1L;

        String path = "/users/" + userTestId;

        when(userService.getUserById(Mockito.anyLong()))
                .thenReturn(userTest);

        mockMvc.perform(delete(path))
                .andExpect(status().isOk())
                .andExpect(content().json(userDefaultDeleteResponse));

        Mockito.verify(userService, times(1)).getUserById(userTestId);
        Mockito.verify(userService, times(1)).deleteUserById(userTestId);
    }


}