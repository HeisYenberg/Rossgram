package com.heisyenberg.imagesservice.controllers;

import static org.mockito.ArgumentMatchers.any;
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
import com.heisyenberg.imagesservice.data.ImageGenerator;
import com.heisyenberg.imagesservice.exceptions.ImageException;
import com.heisyenberg.imagesservice.models.Image;
import com.heisyenberg.imagesservice.models.ImageDto;
import com.heisyenberg.imagesservice.services.ImagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ImagesController.class)
@Import(ImageGenerator.class)
public class ImagesControllerTest {
  @MockitoBean private ImagesService imagesService;
  @Autowired private ImageGenerator imageGenerator;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  private Image testImage;

  @BeforeEach
  public void setup() {
    testImage = imageGenerator.generateImage();
  }

  @Test
  @DisplayName("Check add new image")
  public void checkAddNewImage() throws Exception {
    ImageDto imageDto = new ImageDto(testImage.getImageBase64(), testImage.getUserId());
    when(imagesService.saveImage(any(ImageDto.class))).thenReturn(testImage);
    mockMvc
        .perform(
            post("/images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageDto)))
        .andExpect(status().isCreated())
        .andExpect(content().string(objectMapper.writeValueAsString(testImage)));
  }

  @Test
  @DisplayName("Check successfully deleting image")
  public void checkSuccessfullyDeleteImage() throws Exception {
    doNothing().when(imagesService).deleteImageById(anyLong());
    mockMvc.perform(delete("/images/{image_id}", testImage.getId())).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Check unsuccessfully deleting image")
  public void checkUnsuccessfullyDeleteImage() throws Exception {
    doThrow(ImageException.class).when(imagesService).deleteImageById(anyLong());
    mockMvc
        .perform(delete("/images/{image_id}", testImage.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Check successfully get image")
  public void checkSuccessfullyGetImage() throws Exception {
    when(imagesService.getImageById(anyLong())).thenReturn(testImage.getImageBase64());
    mockMvc
        .perform(get("/images/{image_id}", testImage.getId()))
        .andExpect(status().isOk())
        .andExpect(content().string(testImage.getImageBase64()));
  }

  @Test
  @DisplayName("Check unsuccessfully get image")
  public void checkUnsuccessfullyGetImage() throws Exception {
    when(imagesService.getImageById(anyLong())).thenThrow(ImageException.class);
    mockMvc.perform(get("/images/{image_id}", testImage.getId())).andExpect(status().isNotFound());
  }
}
