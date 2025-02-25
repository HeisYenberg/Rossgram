package com.heisyenberg.usersservice.data;

import com.github.javafaker.Faker;
import com.heisyenberg.usersservice.models.ImageDto;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserCollection;
import com.heisyenberg.usersservice.models.UserDto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class DataGenerator {
  private static final int COLLECTION_SIZE = 10;
  private final Faker faker = new Faker();

  public String generateBearerToken() {
    return faker.internet().uuid();
  }

  public UserDto generateUserDto() {
    return new UserDto(faker.name().username(), faker.internet().password());
  }

  public User generateUser() {
    return User.builder()
        .id(faker.number().randomNumber())
        .username(faker.name().username())
        .password(faker.internet().password())
        .userCollection(new UserCollection(faker.number().randomNumber(), generateUserCollection()))
        .build();
  }

  public List<User> generateUsers() {
    List<User> users = new ArrayList<>(COLLECTION_SIZE);
    for (int i = 0; i < COLLECTION_SIZE; i++) {
      users.add(generateUser());
    }
    return users;
  }

  public long generateUserId() {
    return faker.number().randomNumber();
  }

  public ImageDto generateImageDto() {
    return new ImageDto(String.valueOf(faker.number().randomNumber()));
  }

  public Set<String> generateUserCollection() {
    Set<String> collection = new HashSet<>(COLLECTION_SIZE);
    for (int i = 0; i < COLLECTION_SIZE; i++) {
      collection.add(generateImageDto().getImageId());
    }
    return collection;
  }
}
