package com.heisyenberg.usersservice.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.heisyenberg.usersservice.data.DataGenerator;
import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.filters.JwtAuthenticationFilter;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserCollection;
import com.heisyenberg.usersservice.models.UserDto;
import com.heisyenberg.usersservice.repositories.CollectionsRepository;
import com.heisyenberg.usersservice.repositories.UsersRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UsersService.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DataGenerator.class)
public class UsersServiceTest {
  @Autowired UsersService usersService;
  @MockitoBean private UsersRepository usersRepository;
  @MockitoBean private CollectionsRepository collectionsRepository;
  @MockitoBean private Page<User> page;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private DataGenerator dataGenerator;

  @Test
  @DisplayName("Check successfully create user")
  public void checkSuccessfullyCreateUser() {
    User user = dataGenerator.generateUser();
    UserDto userDto = dataGenerator.generateUserDto();
    when(usersRepository.existsByUsername(anyString())).thenReturn(false);
    when(collectionsRepository.save(any(UserCollection.class))).thenReturn(new UserCollection());
    when(usersRepository.save(any())).thenReturn(user);
    User userCreated = usersService.createUser(userDto);
    Assertions.assertEquals(user, userCreated);
  }

  @Test
  @DisplayName("Check unsuccessfully create user")
  public void checkUnsuccessfullyCreateUser() {
    UserDto userDto = dataGenerator.generateUserDto();
    when(usersRepository.existsByUsername(anyString())).thenReturn(true);
    Assertions.assertThrows(UserException.class, () -> usersService.createUser(userDto));
  }

  @Test
  @DisplayName("Check get users page")
  public void checkGetUsersPage() {
    Page<User> users = new PageImpl<>(dataGenerator.generateUsers());
    when(usersRepository.findAll(any(Pageable.class))).thenReturn(page);
    when(page.getContent()).thenReturn(users.getContent());
    Page<User> resultUsers = usersService.getUsersPage(10, 0);
    Assertions.assertEquals(users.getContent(), resultUsers.getContent());
  }

  @Test
  @DisplayName("Check successfully get by username")
  public void checkSuccessfullyGetByUsername() {
    User user = dataGenerator.generateUser();
    when(usersRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    User resultUser = usersService.getByUsername(user.getUsername());
    Assertions.assertEquals(user, resultUser);
  }

  @Test
  @DisplayName("Check unsuccessfully get by username")
  public void checkUnsuccessfullyGetByUsername() {
    User user = dataGenerator.generateUser();
    when(usersRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    Assertions.assertThrows(
        UserException.class, () -> usersService.getByUsername(user.getUsername()));
  }
}
