package com.heisyenberg.ssrfrontend.controllers;

import com.heisyenberg.ssrfrontend.dtos.Comment;
import com.heisyenberg.ssrfrontend.dtos.CommentData;
import com.heisyenberg.ssrfrontend.dtos.Image;
import com.heisyenberg.ssrfrontend.services.CommentsService;
import com.heisyenberg.ssrfrontend.services.ImagesService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImagesController {
  private final ImagesService imagesService;
  private final CommentsService commentsService;

  @GetMapping("/collections/images/{image_id}")
  public String getImage(@PathVariable("image_id") final Long imageId, final Model model) {
    Image image = imagesService.getImageById(imageId);
    List<Comment> comments = commentsService.getComments(imageId);
    model.addAttribute("image", image);
    model.addAttribute("comments", comments);
    model.addAttribute("commentData", new CommentData());
    return "image";
  }
}
