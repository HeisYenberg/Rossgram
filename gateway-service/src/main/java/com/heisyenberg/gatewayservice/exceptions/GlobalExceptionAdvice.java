package com.heisyenberg.gatewayservice.exceptions;

import feign.FeignException;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvice {
  @ExceptionHandler(NoFallbackAvailableException.class)
  public ResponseEntity<?> handleNoFallbackException(final NoFallbackAvailableException e) {
    Throwable cause = e.getCause();
    if (cause instanceof FeignException feignException) {
      return ResponseEntity.status(feignException.status()).body(feignException.getMessage());
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<?> handleFeignException(final FeignException e) {
    return ResponseEntity.status(e.status()).body(e.getMessage());
  }
}
