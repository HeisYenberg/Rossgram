package com.heisyenberg.gatewayservice.controllers;

import com.heisyenberg.gatewayservice.services.clients.ImagesServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Images management")
public class ImagesController {
  private final ImagesServiceClient imagesServiceClient;

  @GetMapping("/images/{image_id}")
  @Operation(summary = "Get image")
  public ResponseEntity<String> getImage(@PathVariable("image_id") final Long imageId) {
    return imagesServiceClient.getImage(imageId);
  }
}
