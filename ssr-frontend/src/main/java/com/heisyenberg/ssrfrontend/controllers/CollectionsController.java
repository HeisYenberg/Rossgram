package com.heisyenberg.ssrfrontend.controllers;

import com.heisyenberg.ssrfrontend.dtos.Image;
import com.heisyenberg.ssrfrontend.services.CollectionsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectionsController {
  private final CollectionsService collectionsService;

  @PostMapping("/users/me/collections")
  public String addImageToCollection(
      @RequestHeader(value = "Referer", required = false) final String referer,
      @RequestParam("imageFile") final MultipartFile imageFile) {
    collectionsService.addImageToCollection(imageFile);
    return "redirect:" + (referer != null ? referer : "/users");
  }

  @PostMapping("/users/me/collections/{image_id}")
  public String deleteImageFromCollection(
      @RequestHeader(value = "Referer", required = false) final String referer,
      @PathVariable("image_id") final String imageId) {
    collectionsService.deleteImageById(imageId);
    return "redirect:" + (referer != null ? referer : "/users");
  }

  @GetMapping("/users/{user_id}/collections")
  public String getUserCollections(
      @PathVariable("user_id") final String userId, final Model model) {
    String username = collectionsService.getUserById(userId);
    List<Image> images = collectionsService.getUserCollection(userId);
    model.addAttribute("userId", userId);
    model.addAttribute("username", username);
    model.addAttribute("images", images);
    return "collection";
  }
}
