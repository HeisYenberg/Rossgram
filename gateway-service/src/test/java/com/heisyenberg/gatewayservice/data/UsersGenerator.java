package com.heisyenberg.gatewayservice.data;

import com.github.javafaker.Faker;
import com.heisyenberg.gatewayservice.dtos.User;
import com.heisyenberg.gatewayservice.dtos.UserDto;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class UsersGenerator {
  private static final int LIST_SIZE = 10;
  private final Faker faker = new Faker();

  public UserDto createUserDto() {
    return UserDto.builder()
        .username(faker.name().username())
        .password(faker.internet().password())
        .build();
  }

  public User createUser() {
    return User.builder()
        .id(faker.number().randomNumber())
        .username(faker.name().username())
        .password(faker.internet().password())
        .build();
  }

  public List<User> generateUsers() {
    return IntStream.range(0, LIST_SIZE).mapToObj(i -> createUser()).toList();
  }
}
