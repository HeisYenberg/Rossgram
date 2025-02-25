package com.heisyenberg.usersservice.repositories;

import com.heisyenberg.usersservice.data.DataGenerator;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserCollection;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@Import(DataGenerator.class)
public class UsersRepositoryTest {
  @Container
  private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
      new PostgreSQLContainer<>("postgres:15").withInitScript("schema.sql");

  @Autowired private UsersRepository usersRepository;
  @Autowired private DataGenerator dataGenerator;
  private User testUser;

  @DynamicPropertySource
  private static void registerPostgresProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
  }

  @BeforeEach
  public void setup() {
    User user = dataGenerator.generateUser();
    user.setId(null);
    user.setUserCollection(new UserCollection());
    testUser = usersRepository.save(user);
  }

  @AfterEach
  public void teardown() {
    usersRepository.delete(testUser);
  }

  @Test
  @DisplayName("Check save user")
  public void checkSaveUser() {
    User user = dataGenerator.generateUser();
    user.setId(null);
    user.setUserCollection(new UserCollection());
    usersRepository.save(user);
    Optional<User> foundUser = usersRepository.findById(user.getId());
    Assertions.assertTrue(foundUser.isPresent());
    Assertions.assertEquals(user, foundUser.get());
    usersRepository.delete(user);
  }

  @Test
  @DisplayName("Check find by user by id")
  public void checkFindById() {
    Optional<User> foundUser = usersRepository.findById(testUser.getId());
    Assertions.assertTrue(foundUser.isPresent());
    Assertions.assertEquals(testUser, foundUser.get());
  }

  @Test
  @DisplayName("Check update user")
  public void checkUpdateUser() {
    User user = dataGenerator.generateUser();
    user.setId(testUser.getId());
    user.setUserCollection(new UserCollection());
    usersRepository.save(user);
    Optional<User> foundUser = usersRepository.findById(user.getId());
    Assertions.assertTrue(foundUser.isPresent());
    Assertions.assertEquals(testUser, foundUser.get());
  }

  @Test
  @DisplayName("Check delete user")
  public void checkDeleteUser() {
    usersRepository.delete(testUser);
    Optional<User> foundUser = usersRepository.findById(testUser.getId());
    Assertions.assertTrue(foundUser.isEmpty());
  }

  @Test
  @DisplayName("Check find user by username")
  public void checkFindByUsername() {
    Optional<User> foundUser = usersRepository.findByUsername(testUser.getUsername());
    Assertions.assertTrue(foundUser.isPresent());
    Assertions.assertEquals(testUser, foundUser.get());
  }

  @Test
  @DisplayName("Check exist user by id")
  public void checkExistById() {
    boolean exist = usersRepository.existsById(testUser.getId());
    Assertions.assertTrue(exist);
  }
}
