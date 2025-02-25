package com.heisyenberg.ssrfrontend.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "images-client", url = "${gateway.service.url}")
public interface ImagesServiceClient {

  @GetMapping("/images/{image_id}")
  ResponseEntity<String> getImage(@PathVariable("image_id") final Long imageId);
}
