package com.heisyenberg.imagesservice.controllers;

import com.heisyenberg.imagesservice.exceptions.ImageException;
import com.heisyenberg.imagesservice.models.Image;
import com.heisyenberg.imagesservice.models.ImageDto;
import com.heisyenberg.imagesservice.services.ImagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Images management")
public class ImagesController {
  private final ImagesService imagesService;

  @PostMapping
  @Operation(summary = "Add new image")
  public ResponseEntity<Image> addImage(@RequestBody ImageDto imageDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(imagesService.saveImage(imageDto));
  }

  @DeleteMapping("/{image_id}")
  @Operation(summary = "Delete image")
  public ResponseEntity<String> deleteImage(@PathVariable("image_id") Long imageId) {
    try {
      imagesService.deleteImageById(imageId);
      return ResponseEntity.ok("Deleted image");
    } catch (ImageException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{image_id}")
  @Operation(summary = "Get image")
  public ResponseEntity<String> getImage(@PathVariable("image_id") Long imageId) {
    try {
      return ResponseEntity.ok(imagesService.getImageById(imageId));
    } catch (ImageException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
