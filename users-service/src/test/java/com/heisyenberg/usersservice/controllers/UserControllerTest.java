package com.heisyenberg.usersservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heisyenberg.usersservice.data.DataGenerator;
import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.filters.JwtAuthenticationFilter;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserDto;
import com.heisyenberg.usersservice.services.AuthenticationService;
import com.heisyenberg.usersservice.services.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsersController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DataGenerator.class)
public class UserControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockitoBean private AuthenticationService authenticationService;
  @MockitoBean private UsersService usersService;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private DataGenerator dataGenerator;

  @Test
  @DisplayName("Check successfully register user")
  void checkSuccessfullyRegisterUser() throws Exception {
    String token = dataGenerator.generateBearerToken();
    UserDto userDto = dataGenerator.generateUserDto();
    when(authenticationService.registerUser(any(UserDto.class))).thenReturn(token);
    mockMvc
        .perform(
            post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isOk())
        .andExpect(content().string(token));
  }

  @Test
  @DisplayName("Check unsuccessfully register user")
  void checkUnsuccessfullyRegisterUser() throws Exception {
    UserDto userDto = dataGenerator.generateUserDto();
    when(authenticationService.registerUser(any(UserDto.class))).thenThrow(UserException.class);
    mockMvc
        .perform(
            post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Check get users pageable")
  void checkGetUsersPageable() throws Exception {
    Page<User> users = new PageImpl<>(dataGenerator.generateUsers());
    when(usersService.getUsersPage(anyInt(), anyInt())).thenReturn(users);
    mockMvc
        .perform(get("/users?limit={limit}&skip={skip}", 10, 0))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(users)));
  }
}
