package com.heisyenberg.ssrfrontend.controllers;

import com.heisyenberg.ssrfrontend.dtos.CommentData;
import com.heisyenberg.ssrfrontend.services.CommentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentsController {
  private final CommentsService commentsService;

  @PostMapping("/images/{image_id}/comments")
  public String addCommentToImage(
      @RequestHeader(value = "Referer", required = false) final String referer,
      @PathVariable("image_id") final String imageId,
      @Valid @ModelAttribute final CommentData commentData) {
    commentsService.addCommentToImage(imageId, commentData);
    return "redirect:" + (referer != null ? referer : "/users");
  }

  @PostMapping("/images/{image_id}/comments/{comment_id}")
  public String deleteCommentFromImage(
      @RequestHeader(value = "Referer", required = false) final String referer,
      @PathVariable("image_id") final String imageId,
      @PathVariable("comment_id") final Long commentId) {
    commentsService.deleteCommentFromImage(imageId, commentId);
    return "redirect:" + (referer != null ? referer : "/users");
  }
}
