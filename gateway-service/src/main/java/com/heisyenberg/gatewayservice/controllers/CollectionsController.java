package com.heisyenberg.gatewayservice.controllers;

import com.heisyenberg.gatewayservice.dtos.ImageData;
import com.heisyenberg.gatewayservice.dtos.ImageId;
import com.heisyenberg.gatewayservice.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Collections management")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectionsController {
  private final UsersService usersService;

  @PostMapping("/me/collections")
  @Operation(summary = "Add image to user collection")
  public ResponseEntity<?> addImageToCollection(
      @RequestHeader("Authorization") final String authorization,
      @RequestBody final ImageData imageData) {
    return usersService.addImageToCollection(authorization, imageData);
  }

  @DeleteMapping("/me/collections")
  @Operation(summary = "Delete image from user collection")
  public ResponseEntity<?> deleteImageFromCollection(
      @RequestHeader("Authorization") final String authorization,
      @RequestBody final ImageId imageId) {
    return usersService.deleteImageFromCollection(authorization, imageId);
  }

  @GetMapping("/{user_id}/collections")
  @Operation(summary = "Get user collection")
  public ResponseEntity<?> getUsersCollection(@PathVariable("user_id") final String userId) {
    return usersService.getUsersCollection(userId);
  }
}
