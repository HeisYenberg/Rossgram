package com.heisyenberg.commentsservice.controllers;

import com.heisyenberg.commentsservice.exceptions.CommentException;
import com.heisyenberg.commentsservice.models.Comment;
import com.heisyenberg.commentsservice.models.CommentDto;
import com.heisyenberg.commentsservice.services.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@RequestMapping("/images/{image_id}/comments")
@Tag(name = "Comments management")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentsController {
  private final CommentsService commentsService;

  @PostMapping
  @Operation(summary = "Add comment to image")
  public ResponseEntity<Comment> addComment(
      @PathVariable("image_id") final String imageId, @RequestBody final CommentDto commentDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(commentsService.addCommentToImage(imageId, commentDto));
  }

  @GetMapping
  @Operation(summary = "Get image comments")
  public ResponseEntity<List<Comment>> getImageComments(
      @PathVariable("image_id") final String imageId) {
    return ResponseEntity.ok(commentsService.getImageComments(imageId));
  }

  @GetMapping("/{comment_id}")
  @Operation(summary = "Get image comment")
  public ResponseEntity<Comment> getImageComment(
      @PathVariable("image_id") final String imageId,
      @PathVariable("comment_id") final Long commentId) {
    try {
      return ResponseEntity.ok(commentsService.getImageComment(commentId, imageId));
    } catch (CommentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{comment_id}")
  @Operation(summary = "Delete image comment")
  public ResponseEntity<String> deleteImageComment(
      @PathVariable("image_id") final String imageId,
      @PathVariable("comment_id") final Long commentId) {
    try {
      commentsService.deleteComment(commentId, imageId);
      return ResponseEntity.ok("Comment deleted");
    } catch (CommentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
