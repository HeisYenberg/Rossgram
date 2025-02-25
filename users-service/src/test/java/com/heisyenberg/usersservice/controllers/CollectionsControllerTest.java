package com.heisyenberg.usersservice.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heisyenberg.usersservice.data.DataGenerator;
import com.heisyenberg.usersservice.exceptions.CollectionImageException;
import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.filters.JwtAuthenticationFilter;
import com.heisyenberg.usersservice.models.ImageDto;
import com.heisyenberg.usersservice.services.CollectionsService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CollectionsController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(DataGenerator.class)
public class CollectionsControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockitoBean private CollectionsService collectionsService;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private DataGenerator dataGenerator;
  private Long userId;

  @BeforeEach
  public void setup() {
    userId = dataGenerator.generateUserId();
  }

  @Test
  @DisplayName("Check successfully get user collection")
  public void checkSuccessfullyGetUserCollection() throws Exception {
    Set<String> userCollection = dataGenerator.generateUserCollection();
    when(collectionsService.getUserCollection(anyLong())).thenReturn(userCollection);
    mockMvc
        .perform(get("/users/{user_id}/collections", userId))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(userCollection)));
  }

  @Test
  @DisplayName("Check unsuccessfully get user collection")
  public void checkUnsuccessfullyGetUserCollection() throws Exception {
    when(collectionsService.getUserCollection(anyLong())).thenThrow(UserException.class);
    mockMvc.perform(get("/users/{user_id}/collections", userId)).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Check adding image to user collection successfully")
  public void checkSuccessfullyAddImageToUserCollection() throws Exception {
    ImageDto imageDto = dataGenerator.generateImageDto();
    doNothing().when(collectionsService).addImageToCollection(userId, imageDto);
    mockMvc
        .perform(
            post("/users/{user_id}/collections", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageDto)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Check adding image to user collection notFound")
  public void checkAddImageToUserCollectionNotFound() throws Exception {
    ImageDto imageDto = dataGenerator.generateImageDto();
    doThrow(UserException.class).when(collectionsService).addImageToCollection(userId, imageDto);
    mockMvc
        .perform(
            post("/users/{user_id}/collections", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageDto)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Check adding image to user collection badRequest")
  public void checkAddImageToUserCollectionBadRequest() throws Exception {
    ImageDto imageDto = dataGenerator.generateImageDto();
    doThrow(CollectionImageException.class)
        .when(collectionsService)
        .addImageToCollection(userId, imageDto);
    mockMvc
        .perform(
            post("/users/{user_id}/collections", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Check delete image to user collection successfully")
  public void checkSuccessfullyDeleteImageToUserCollection() throws Exception {
    ImageDto imageDto = dataGenerator.generateImageDto();
    doNothing().when(collectionsService).removeImageFromCollection(userId, imageDto);
    mockMvc
        .perform(
            delete("/users/{user_id}/collections", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageDto)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Check delete image to user collection badRequest")
  public void checkDeleteImageToUserCollectionBadRequest() throws Exception {
    ImageDto imageDto = dataGenerator.generateImageDto();
    doThrow(CollectionImageException.class)
        .when(collectionsService)
        .removeImageFromCollection(userId, imageDto);
    mockMvc
        .perform(
            delete("/users/{user_id}/collections", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageDto)))
        .andExpect(status().isBadRequest());
  }
}
