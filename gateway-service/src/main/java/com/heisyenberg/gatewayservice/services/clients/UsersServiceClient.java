package com.heisyenberg.gatewayservice.services.clients;

import com.heisyenberg.gatewayservice.dtos.ImageId;
import com.heisyenberg.gatewayservice.dtos.User;
import com.heisyenberg.gatewayservice.dtos.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users-service", url = "${users-service.url}")
public interface UsersServiceClient {
  @PostMapping("/authorization")
  ResponseEntity<String> authorize(@RequestBody final UserDto userDto);

  @PostMapping("/registration")
  ResponseEntity<String> register(@RequestBody final UserDto userDto);

  @PostMapping("/users/{user_id}/collections")
  ResponseEntity<String> addImageToCollection(
      @PathVariable("user_id") final String userId, @RequestBody final ImageId imageId);

  @DeleteMapping("/users/{user_id}/collections")
  ResponseEntity<String> deleteImageFromCollection(
      @PathVariable("user_id") final String userId, @RequestBody final ImageId imageId);

  @GetMapping("/users/{user_id}/collections")
  @Retry(name = "gateway")
  @CircuitBreaker(name = "gateway", fallbackMethod = "fallback")
  ResponseEntity<Set<String>> getUserCollections(@PathVariable("user_id") final String userId);

  @GetMapping("/users/{user_id}")
  @Retry(name = "gateway")
  @CircuitBreaker(name = "gateway", fallbackMethod = "fallback")
  ResponseEntity<String> getUserById(@PathVariable("user_id") final String userId);

  @GetMapping("/users")
  @Retry(name = "gateway")
  @CircuitBreaker(name = "gateway", fallbackMethod = "fallback")
  ResponseEntity<Page<User>> getUsers(@RequestParam final int limit, @RequestParam final int skip);

  default ResponseEntity<?> fallback(final Exception e) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
  }
}
