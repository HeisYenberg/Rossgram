package com.heisyenberg.usersservice.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.heisyenberg.usersservice.data.DataGenerator;
import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.filters.JwtAuthenticationFilter;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(AuthenticationService.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DataGenerator.class)
public class AuthorizationServiceTest {
  @Autowired private AuthenticationService authenticationService;
  @MockitoBean private UsersService usersService;
  @MockitoBean private UserDetailsService userDetailsService;
  @MockitoBean private JwtService jwtService;
  @MockitoBean private PasswordEncoder passwordEncoder;
  @MockitoBean private AuthenticationManager authenticationManager;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private DataGenerator dataGenerator;
  private UserDto userDto;
  private User user;
  private String fakeToken;

  @BeforeEach
  public void setup() {
    userDto = dataGenerator.generateUserDto();
    user = dataGenerator.generateUser();
    fakeToken = dataGenerator.generateBearerToken();
    when(usersService.userDetailsService()).thenReturn(userDetailsService);
    when(passwordEncoder.encode(anyString())).thenReturn(userDto.getPassword());
    when(jwtService.generateToken(any(User.class))).thenReturn(fakeToken);
  }

  @Test
  @DisplayName("Check successfully authenticate")
  void checkSuccessfullyAuthenticate() {
    when(userDetailsService.loadUserByUsername(userDto.getUsername())).thenReturn(user);
    String token = authenticationService.authenticate(userDto);
    Assertions.assertEquals(fakeToken, token);
  }

  @Test
  @DisplayName("Check unsuccessfully authenticate")
  void checkUnsuccessfullyAuthenticate() {
    when(userDetailsService.loadUserByUsername(userDto.getUsername()))
        .thenThrow(UserException.class);
    when(jwtService.generateToken(any(User.class))).thenReturn(fakeToken);
    Assertions.assertThrows(UserException.class, () -> authenticationService.authenticate(userDto));
  }

  @Test
  @DisplayName("Check successfully register user")
  void checkSuccessfullyRegisterUser() {
    when(usersService.createUser(any(UserDto.class))).thenReturn(user);
    String token = authenticationService.registerUser(userDto);
    Assertions.assertEquals(fakeToken, token);
  }

  @Test
  @DisplayName("Check unsuccessfully register user")
  void checkUnsuccessfullyRegisterUser() {
    when(usersService.createUser(any(UserDto.class))).thenThrow(UserException.class);
    Assertions.assertThrows(UserException.class, () -> authenticationService.registerUser(userDto));
  }
}
