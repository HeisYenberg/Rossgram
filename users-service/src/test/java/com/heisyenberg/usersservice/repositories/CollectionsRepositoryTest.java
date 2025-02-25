package com.heisyenberg.usersservice.repositories;

import com.heisyenberg.usersservice.data.DataGenerator;
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
public class CollectionsRepositoryTest {
  @Container
  private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
      new PostgreSQLContainer<>("postgres:15").withInitScript("schema.sql");

  @Autowired private CollectionsRepository collectionsRepository;
  @Autowired private DataGenerator dataGenerator;
  private UserCollection testCollection;

  @DynamicPropertySource
  private static void registerPostgresProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
  }

  @BeforeEach
  public void setup() {
    UserCollection userCollection = dataGenerator.generateUser().getUserCollection();
    userCollection.setId(null);
    testCollection = collectionsRepository.save(userCollection);
  }

  @AfterEach
  public void teardown() {
    collectionsRepository.delete(testCollection);
  }

  @Test
  @DisplayName("Check save collection")
  public void checkSaveCollection() {
    UserCollection userCollection = dataGenerator.generateUser().getUserCollection();
    userCollection.setId(null);
    UserCollection savedCollection = collectionsRepository.save(userCollection);
    userCollection.setId(savedCollection.getId());
    Optional<UserCollection> foundCollection =
        collectionsRepository.findById(userCollection.getId());
    Assertions.assertTrue(foundCollection.isPresent());
    Assertions.assertEquals(userCollection.getId(), foundCollection.get().getId());
    collectionsRepository.delete(userCollection);
  }

  @Test
  @DisplayName("Check find collection by id")
  public void checkFindCollectionById() {
    Optional<UserCollection> foundCollection =
        collectionsRepository.findById(testCollection.getId());
    Assertions.assertTrue(foundCollection.isPresent());
    Assertions.assertEquals(testCollection, foundCollection.get());
  }

  @Test
  @DisplayName("Check update collection")
  public void checkUpdateCollection() {
    UserCollection userCollection = dataGenerator.generateUser().getUserCollection();
    userCollection.setId(testCollection.getId());
    collectionsRepository.save(userCollection);
    Optional<UserCollection> foundCollection =
        collectionsRepository.findById(userCollection.getId());
    Assertions.assertTrue(foundCollection.isPresent());
    Assertions.assertEquals(testCollection, foundCollection.get());
  }

  @Test
  @DisplayName("Check delete collection")
  public void checkDeleteCollection() {
    collectionsRepository.delete(testCollection);
    Optional<UserCollection> foundCollection =
        collectionsRepository.findById(testCollection.getId());
    Assertions.assertTrue(foundCollection.isEmpty());
  }
}
