package com.heisyenberg.gatewayservice.controllers;

import com.heisyenberg.gatewayservice.dtos.User;
import com.heisyenberg.gatewayservice.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Users management")
public class UsersController {
  private final UsersService usersService;

  @GetMapping("/{user_id}")
  @Operation(summary = "Get user by id")
  public ResponseEntity<String> getUserById(@PathVariable("user_id") final String userId) {
    return usersService.getUserById(userId);
  }

  @GetMapping
  @Operation(summary = "Get users pageable")
  public ResponseEntity<Page<User>> getUsers(
      @RequestParam(defaultValue = "10") final int limit,
      @RequestParam(defaultValue = "0") final int skip) {
    return usersService.getUsers(limit, skip);
  }
}
