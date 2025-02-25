package com.heisyenberg.usersservice.controllers;

import com.heisyenberg.usersservice.exceptions.UserException;
import com.heisyenberg.usersservice.models.UserDto;
import com.heisyenberg.usersservice.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Authorization")
public class AuthorizationController {
  private final AuthenticationService authenticationService;

  @PostMapping
  @Operation(summary = "User authorization")
  public ResponseEntity<String> authorize(@RequestBody final UserDto userDto) {
    try {
      return ResponseEntity.ok(authenticationService.authenticate(userDto));
    } catch (UserException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

  @GetMapping
  @Operation(summary = "Check if user authorized")
  public ResponseEntity<String> checkAuthorization() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken)) {
      return ResponseEntity.ok("Authorized");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }
  }
}
