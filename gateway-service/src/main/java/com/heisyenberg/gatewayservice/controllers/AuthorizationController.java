package com.heisyenberg.gatewayservice.controllers;

import com.heisyenberg.gatewayservice.dtos.UserDto;
import com.heisyenberg.gatewayservice.services.clients.UsersServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Authorization")
public class AuthorizationController {
  private final UsersServiceClient usersServiceClient;

  @PostMapping("/authorization")
  @Operation(summary = "User authorization")
  public ResponseEntity<?> authorize(@RequestBody final UserDto userDto) {
    return usersServiceClient.authorize(userDto);
  }

  @PostMapping("/registration")
  @Operation(summary = "User registration")
  public ResponseEntity<?> register(@RequestBody final UserDto userDto) {
    return usersServiceClient.register(userDto);
  }
}
