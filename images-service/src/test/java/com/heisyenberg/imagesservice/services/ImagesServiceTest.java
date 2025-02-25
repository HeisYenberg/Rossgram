package com.heisyenberg.imagesservice.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.heisyenberg.imagesservice.data.ImageGenerator;
import com.heisyenberg.imagesservice.exceptions.ImageException;
import com.heisyenberg.imagesservice.models.Image;
import com.heisyenberg.imagesservice.models.ImageDto;
import com.heisyenberg.imagesservice.repositories.ImagesRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(ImagesService.class)
@ExtendWith(MockitoExtension.class)
@Import(ImageGenerator.class)
public class ImagesServiceTest {
  @MockitoBean private ImagesRepository imagesRepository;
  @Autowired private ImagesService imagesService;
  @Autowired private ImageGenerator imageGenerator;
  private Image testImage;

  @BeforeEach
  public void setup() {
    testImage = imageGenerator.generateImage();
  }

  @Test
  @DisplayName("Check saving image")
  public void checkSaveImage() {
    when(imagesRepository.save(any(Image.class))).thenReturn(testImage);
    ImageDto imageDto = new ImageDto(testImage.getImageBase64(), testImage.getUserId());
    Image result = imagesService.saveImage(imageDto);
    Assertions.assertEquals(testImage, result);
  }

  @Test
  @DisplayName("Check successfully get image by id")
  public void checkSuccessfullyGetImageById() {
    when(imagesRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(testImage));
    String image = imagesService.getImageById(testImage.getId());
    Assertions.assertEquals(testImage.getImageBase64(), image);
  }

  @Test
  @DisplayName("Check unsuccessfully get image by id")
  public void checkUnsuccessfullyGetImageById() {
    when(imagesRepository.findByIdAndDeletedFalse(anyLong())).thenThrow(ImageException.class);
    Assertions.assertThrows(
        ImageException.class, () -> imagesService.getImageById(testImage.getId()));
  }

  @Test
  @DisplayName("Check successfully delete image by id")
  public void checkSuccessfullyDeleteImageById() {
    testImage.setDeleted(true);
    when(imagesRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(testImage));
    Assertions.assertDoesNotThrow(() -> imagesService.deleteImageById(testImage.getId()));
  }

  @Test
  @DisplayName("Check unsuccessfully delete image by id")
  public void checkUnsuccessfullyDeleteImageById() {
    testImage.setDeleted(true);
    when(imagesRepository.findByIdAndDeletedFalse(anyLong())).thenThrow(ImageException.class);
    Assertions.assertThrows(
        ImageException.class, () -> imagesService.deleteImageById(testImage.getId()));
  }
}
