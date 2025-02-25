package com.heisyenberg.gatewayservice.services.clients;

import com.heisyenberg.gatewayservice.dtos.Image;
import com.heisyenberg.gatewayservice.dtos.ImageDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "images-service", url = "${images-service.url}")
public interface ImagesServiceClient {
  @PostMapping("/images")
  ResponseEntity<Image> addImage(@RequestBody ImageDto imageDto);

  @DeleteMapping("/images/{image_id}")
  ResponseEntity<String> deleteImage(@PathVariable("image_id") Long imageId);

  @GetMapping("/images/{image_id}")
  @Retry(name = "gateway")
  @CircuitBreaker(name = "gateway", fallbackMethod = "fallback")
  ResponseEntity<String> getImage(@PathVariable("image_id") Long imageId);

  default ResponseEntity<?> fallback(final Exception e) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
  }
}
