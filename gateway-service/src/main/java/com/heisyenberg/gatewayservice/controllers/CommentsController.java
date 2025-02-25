package com.heisyenberg.gatewayservice.controllers;

import com.heisyenberg.gatewayservice.dtos.Comment;
import com.heisyenberg.gatewayservice.dtos.CommentData;
import com.heisyenberg.gatewayservice.services.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@RequestMapping("/images/{image_id}/comments")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Comments management")
public class CommentsController {
  private final CommentsService commentsService;

  @PostMapping
  @Operation(summary = "Add comment to image")
  public ResponseEntity<?> createComment(
      @RequestHeader("Authorization") final String authorization,
      @PathVariable("image_id") final String imageId,
      @RequestBody final CommentData commentData) {
    return commentsService.createComment(authorization, imageId, commentData);
  }

  @GetMapping
  @Operation(summary = "Get image comments")
  public ResponseEntity<List<Comment>> getComments(@PathVariable("image_id") final String imageId) {
    return commentsService.getComments(imageId);
  }

  @DeleteMapping("/{comment_id}")
  @Operation(summary = "Delete comment from image")
  public ResponseEntity<String> deleteComment(
      @RequestHeader("Authorization") final String authorization,
      @PathVariable("image_id") final String imageId,
      @PathVariable("comment_id") final Long commentId) {
    return commentsService.deleteComment(authorization, imageId, commentId);
  }
}
