package com.heisyenberg.ssrfrontend.services.clients;

import com.heisyenberg.ssrfrontend.dtos.Comment;
import com.heisyenberg.ssrfrontend.dtos.CommentData;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "comments-client", url = "${gateway.service.url}")
public interface CommentsServiceClient {

  @PostMapping("/images/{image_id}/comments")
  ResponseEntity<Comment> createComment(
      @RequestHeader("Authorization") final String authorization,
      @PathVariable("image_id") final String imageId,
      @RequestBody final CommentData commentData);

  @GetMapping("/images/{image_id}/comments")
  ResponseEntity<List<Comment>> getComments(@PathVariable("image_id") final String imageId);

  @DeleteMapping("/images/{image_id}/comments/{comment_id}")
  ResponseEntity<String> deleteComment(
      @RequestHeader("Authorization") final String authorization,
      @PathVariable("image_id") final String imageId,
      @PathVariable("comment_id") final Long commentId);
}
