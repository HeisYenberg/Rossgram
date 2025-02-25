package com.heisyenberg.usersservice.controllers;

import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.models.User;
import com.heisyenberg.usersservice.models.UserDto;
import com.heisyenberg.usersservice.services.AuthenticationService;
import com.heisyenberg.usersservice.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "User management")
public class UsersController {
  private final AuthenticationService authenticationService;
  private final UsersService usersService;

  @PostMapping("/registration")
  @Operation(summary = "User registration")
  public ResponseEntity<String> registerUser(@RequestBody final UserDto userDto) {
    try {
      return ResponseEntity.ok(authenticationService.registerUser(userDto));
    } catch (UserException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/users/{user_id}")
  @Operation(summary = "Get user by id")
  public ResponseEntity<String> getUserById(@PathVariable("user_id") final String userId) {
    try {
      return ResponseEntity.ok(usersService.getUsernameById(Long.parseLong(userId)));
    } catch (UserException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/users")
  @Operation(summary = "Get users pageable")
  public ResponseEntity<Page<User>> getUsers(
      @RequestParam final int limit, @RequestParam final int skip) {
    return ResponseEntity.ok(usersService.getUsersPage(limit, skip));
  }
}
