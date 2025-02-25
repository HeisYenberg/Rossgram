package com.heisyenberg.usersservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heisyenberg.usersservice.data.DataGenerator;
import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.filters.JwtAuthenticationFilter;
import com.heisyenberg.usersservice.models.UserDto;
import com.heisyenberg.usersservice.services.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthorizationController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DataGenerator.class)
public class AuthorizationControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockitoBean private AuthenticationService authenticationService;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private DataGenerator dataGenerator;

  @Test
  @DisplayName("Check authorize successfully")
  public void checkAuthorizeSuccessfully() throws Exception {
    String token = dataGenerator.generateBearerToken();
    UserDto userDto = dataGenerator.generateUserDto();
    when(authenticationService.authenticate(any(UserDto.class))).thenReturn(token);
    mockMvc
        .perform(
            post("/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isOk())
        .andExpect(content().string(token));
  }

  @Test
  @DisplayName("Check authorize unsuccessfully")
  public void checkAuthorizeUnsuccessfully() throws Exception {
    UserDto userDto = dataGenerator.generateUserDto();
    when(authenticationService.authenticate(any(UserDto.class))).thenThrow(UserException.class);
    mockMvc
        .perform(
            post("/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Check authorization checking ok")
  @WithMockUser
  public void checkAuthorizationCheckingOk() throws Exception {
    mockMvc.perform(get("/authorization")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Check authorization checking unauthorized")
  @WithAnonymousUser
  public void checkAuthorizationCheckingUnauthorized() throws Exception {
    mockMvc.perform(get("/authorization")).andExpect(status().isUnauthorized());
  }
}
