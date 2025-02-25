package com.heisyenberg.ssrfrontend.services.clients;

import com.heisyenberg.ssrfrontend.dtos.ImageData;
import com.heisyenberg.ssrfrontend.dtos.ImageId;
import com.heisyenberg.ssrfrontend.dtos.User;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users-client", url = "${gateway.service.url}")
public interface UsersServiceClient {

  @PostMapping("/users/me/collections")
  ResponseEntity<String> addImageToCollection(
      @RequestHeader("Authorization") final String authorization,
      @RequestBody final ImageData imageData);

  @DeleteMapping("/users/me/collections")
  ResponseEntity<String> deleteImageFromCollection(
      @RequestHeader("Authorization") final String authorization,
      @RequestBody final ImageId imageId);

  @GetMapping("/users/{user_id}/collections")
  ResponseEntity<Set<String>> getUsersCollection(@PathVariable("user_id") final String userId);

  @GetMapping("/users/{user_id}")
  ResponseEntity<String> getUserById(@PathVariable("user_id") final String userId);

  @GetMapping("/users")
  ResponseEntity<Page<User>> getUsers(
      @RequestParam(required = false) final Integer limit,
      @RequestParam(required = false) final Integer skip);
}
