package com.example.railwaystationapprest.controller;

import com.example.railwaystationapprest.dto.CreateUserDto;
import com.example.railwaystationapprest.mapper.UserMapper;
import com.example.railwaystationapprest.model.User;
import com.example.railwaystationapprest.model.UserType;
import com.example.railwaystationapprest.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.web.servlet.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;


    @Test
    void createUser() throws Exception {
        CreateUserDto request =new CreateUserDto("email","1234","user","user",UserType.SOCIAL);
        User user =new User("email","1234",UserType.SOCIAL,"user","user");
        user.setId(1l);
        when(userService.create(any())).thenReturn(new User("email","1234",UserType.SOCIAL,"user","user"));
        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


    }
}