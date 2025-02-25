package com.heisyenberg.imagesservice.repositories;

import com.heisyenberg.imagesservice.data.ImageGenerator;
import com.heisyenberg.imagesservice.models.Image;
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
@Import(ImageGenerator.class)
public class ImagesRepositoriesTest {
  @Container
  private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
      new PostgreSQLContainer<>("postgres:15").withInitScript("schema.sql");

  @Autowired private ImagesRepository imagesRepository;
  @Autowired private ImageGenerator imageGenerator;
  private Image testImage;

  @DynamicPropertySource
  private static void registerPostgresProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
  }

  @BeforeEach
  public void setup() {
    testImage = imageGenerator.generateImage();
    testImage.setId(null);
    imagesRepository.save(testImage);
  }

  @AfterEach
  public void teardown() {
    imagesRepository.delete(testImage);
  }

  @Test
  @DisplayName("Check save image")
  public void checkSaveImage() {
    Image image = imageGenerator.generateImage();
    image.setId(null);
    Image savedImage = imagesRepository.save(image);
    image.setId(savedImage.getId());
    Optional<Image> foundImage = imagesRepository.findById(savedImage.getId());
    Assertions.assertTrue(foundImage.isPresent());
    Assertions.assertEquals(image, foundImage.get());
    imagesRepository.delete(image);
  }

  @Test
  @DisplayName("Check find image by id")
  public void checkFindImageById() {
    Optional<Image> foundImage = imagesRepository.findById(testImage.getId());
    Assertions.assertTrue(foundImage.isPresent());
    Assertions.assertEquals(testImage, foundImage.get());
  }

  @Test
  @DisplayName("Check update image")
  public void checkUpdateImage() {
    Image image = imageGenerator.generateImage();
    image.setId(testImage.getId());
    Image updatedImage = imagesRepository.save(image);
    Assertions.assertEquals(image, updatedImage);
  }

  @Test
  @DisplayName("Check delete image")
  public void checkDeleteImage() {
    imagesRepository.delete(testImage);
    Optional<Image> foundImage = imagesRepository.findById(testImage.getId());
    Assertions.assertTrue(foundImage.isEmpty());
  }

  @Test
  @DisplayName("Check find by id and not deleted")
  public void checkFindByIdAndNotDeleted() {
    Optional<Image> foundImage = imagesRepository.findByIdAndDeletedFalse(testImage.getId());
    Assertions.assertTrue(foundImage.isPresent());
    Assertions.assertEquals(testImage, foundImage.get());
  }
}
