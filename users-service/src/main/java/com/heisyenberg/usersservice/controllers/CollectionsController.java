package com.heisyenberg.usersservice.controllers;

import com.heisyenberg.usersservice.exceptions.CollectionImageException;
import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.models.ImageDto;
import com.heisyenberg.usersservice.services.CollectionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
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
@RequestMapping("/users/{user_id}/collections")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Users collections")
public class CollectionsController {
  private final CollectionsService collectionsService;

  @GetMapping
  @Operation(summary = "Get user collection")
  public ResponseEntity<Set<String>> getUserCollection(@PathVariable("user_id") final Long userId) {
    try {
      return ResponseEntity.ok(collectionsService.getUserCollection(userId));
    } catch (UserException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  @Operation(summary = "Add image to user collection")
  public ResponseEntity<String> addImageToCollection(
      @PathVariable("user_id") final Long userId, @RequestBody final ImageDto imageDto) {
    try {
      collectionsService.addImageToCollection(userId, imageDto);
      return ResponseEntity.status(HttpStatus.CREATED).body("Image added to collection");
    } catch (UserException e) {
      return ResponseEntity.notFound().build();
    } catch (CollectionImageException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping
  @Operation(summary = "Delete image from user collection")
  public ResponseEntity<String> deleteImageFromCollection(
      @PathVariable("user_id") final Long userId, @RequestBody final ImageDto imageDto) {
    try {
      collectionsService.removeImageFromCollection(userId, imageDto);
      return ResponseEntity.ok("Image deleted from collection");
    } catch (CollectionImageException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
