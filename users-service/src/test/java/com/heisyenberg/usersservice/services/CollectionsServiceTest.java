package com.heisyenberg.usersservice.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.heisyenberg.usersservice.data.DataGenerator;
import com.heisyenberg.usersservice.exceptions.CollectionImageException;
import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.filters.JwtAuthenticationFilter;
import com.heisyenberg.usersservice.models.ImageDto;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserCollection;
import com.heisyenberg.usersservice.repositories.CollectionsRepository;
import com.heisyenberg.usersservice.repositories.UsersRepository;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(CollectionsService.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DataGenerator.class)
public class CollectionsServiceTest {
  @Autowired CollectionsService collectionsService;
  @MockitoBean private UsersRepository usersRepository;
  @MockitoBean private CollectionsRepository collectionsRepository;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private DataGenerator dataGenerator;
  private Long userId;
  private User user;

  @BeforeEach
  public void setup() {
    userId = dataGenerator.generateUserId();
    user = dataGenerator.generateUser();
  }

  @Test
  @DisplayName("Check successfully get user collection")
  public void checkSuccessfullyGetUserCollection() {
    when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
    Set<String> collection = collectionsService.getUserCollection(userId);
    Assertions.assertEquals(user.getUserCollection().getImagesIds(), collection);
  }

  @Test
  @DisplayName("Check unsuccessfully get user collection")
  public void checkUnsuccessfullyGetUserCollection() {
    when(usersRepository.findById(userId)).thenThrow(UserException.class);
    Assertions.assertThrows(
        UserException.class, () -> collectionsService.getUserCollection(userId));
  }

  @Test
  @DisplayName("Check successfully adding image")
  public void checkSuccessfullyAddingImage() {
    ImageDto imageDto = dataGenerator.generateImageDto();
    when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
    when(collectionsRepository.save(any(UserCollection.class)))
        .thenReturn(user.getUserCollection());
    Assertions.assertDoesNotThrow(() -> collectionsService.addImageToCollection(userId, imageDto));
  }

  @Test
  @DisplayName("Check adding image for user not found")
  public void checkAddingImageForUserNotFound() {
    ImageDto imageDto = dataGenerator.generateImageDto();
    when(usersRepository.findById(userId)).thenThrow(UserException.class);
    Assertions.assertThrows(
        UserException.class, () -> collectionsService.addImageToCollection(userId, imageDto));
  }

  @Test
  @DisplayName("Check unsuccessfully adding image")
  public void checkUnsuccessfullyAddingImage() {
    ImageDto imageDto = new ImageDto(user.getUserCollection().getImagesIds().iterator().next());
    when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
    when(collectionsRepository.save(any(UserCollection.class)))
        .thenReturn(user.getUserCollection());
    Assertions.assertThrows(
        CollectionImageException.class,
        () -> collectionsService.addImageToCollection(userId, imageDto));
  }

  @Test
  @DisplayName("Check successfully delete image")
  public void checkSuccessfullyDeleteImage() {
    ImageDto imageDto = new ImageDto(user.getUserCollection().getImagesIds().iterator().next());
    when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
    when(collectionsRepository.save(any(UserCollection.class)))
        .thenReturn(user.getUserCollection());
    Assertions.assertDoesNotThrow(
        () -> collectionsService.removeImageFromCollection(userId, imageDto));
  }

  @Test
  @DisplayName("Check deleting image for user not found")
  public void checkDeletingImageForUserNotFound() {
    ImageDto imageDto = dataGenerator.generateImageDto();
    when(usersRepository.findById(userId)).thenThrow(UserException.class);
    Assertions.assertThrows(
        UserException.class, () -> collectionsService.removeImageFromCollection(userId, imageDto));
  }

  @Test
  @DisplayName("Check unsuccessfully delete image")
  public void checkUnsuccessfullyDeleteImage() {
    ImageDto imageDto = new ImageDto(user.getUserCollection().getImagesIds().iterator().next());
    when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
    when(collectionsRepository.save(any(UserCollection.class)))
        .thenReturn(user.getUserCollection());
    Assertions.assertThrows(
        CollectionImageException.class,
        () -> collectionsService.addImageToCollection(userId, imageDto));
  }
}
