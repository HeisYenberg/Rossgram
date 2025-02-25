package com.heisyenberg.gatewayservice.services.clients;

import com.heisyenberg.gatewayservice.dtos.Comment;
import com.heisyenberg.gatewayservice.dtos.CommentDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "comments-service", url = "${comments-service.url}")
public interface CommentsServiceClient {
  @PostMapping("/images/{image_id}/comments")
  ResponseEntity<Comment> addComment(
      @PathVariable("image_id") final String imageId, @RequestBody final CommentDto commentDto);

  @GetMapping("/images/{image_id}/comments")
  @Retry(name = "gateway")
  @CircuitBreaker(name = "gateway", fallbackMethod = "fallback")
  ResponseEntity<List<Comment>> getImageComments(@PathVariable("image_id") final String imageId);

  @GetMapping("/images/{image_id}/comments/{comment_id}")
  @Retry(name = "gateway")
  @CircuitBreaker(name = "gateway", fallbackMethod = "fallback")
  ResponseEntity<Comment> getImageComment(
      @PathVariable("image_id") final String imageId,
      @PathVariable("comment_id") final Long commentId);

  @DeleteMapping("/images/{image_id}/comments/{comment_id}")
  ResponseEntity<String> deleteImageComment(
      @PathVariable("image_id") final String imageId,
      @PathVariable("comment_id") final Long commentId);

  default ResponseEntity<?> fallback(final Exception e) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
  }
}
