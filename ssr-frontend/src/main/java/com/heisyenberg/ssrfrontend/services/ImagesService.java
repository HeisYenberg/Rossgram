package com.heisyenberg.ssrfrontend.services;

import com.heisyenberg.ssrfrontend.dtos.Image;
import com.heisyenberg.ssrfrontend.services.clients.ImagesServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImagesService {
  private final ImagesServiceClient imagesServiceClient;

  public Image getImageById(final Long imageId) {
    return Image.builder()
        .id(imageId)
        .imageBase64(imagesServiceClient.getImage(imageId).getBody())
        .build();
  }
}
