package com.heisyenberg.imagesservice.services;

import com.heisyenberg.imagesservice.exceptions.ImageException;
import com.heisyenberg.imagesservice.models.Image;
import com.heisyenberg.imagesservice.models.ImageDto;
import com.heisyenberg.imagesservice.repositories.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImagesService {
  private final ImagesRepository imagesRepository;

  public Image saveImage(final ImageDto imageDto) {
    final Image image =
        Image.builder()
            .userId(imageDto.getUserId())
            .imageBase64(imageDto.getImageBase64())
            .deleted(false)
            .build();
    return imagesRepository.save(image);
  }

  public String getImageById(final Long id) {
    return imagesRepository
        .findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new ImageException("Image not exists or was deleted"))
        .getImageBase64();
  }

  public void deleteImageById(final Long id) {
    final Image image =
        imagesRepository
            .findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new ImageException("Image not exists or was deleted"));
    image.setDeleted(true);
    imagesRepository.save(image);
  }
}
