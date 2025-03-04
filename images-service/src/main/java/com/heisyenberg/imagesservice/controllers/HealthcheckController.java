package com.heisyenberg.imagesservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {
  @GetMapping("/healthcheck")
  public ResponseEntity<String> healthcheck() {
    return ResponseEntity.ok("Service is healthy");
  }
}
