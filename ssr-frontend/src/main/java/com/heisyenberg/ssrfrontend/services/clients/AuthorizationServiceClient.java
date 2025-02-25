package com.heisyenberg.ssrfrontend.services.clients;

import com.heisyenberg.ssrfrontend.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authorization-client", url = "${gateway.service.url}")
public interface AuthorizationServiceClient {

  @PostMapping("/authorization")
  ResponseEntity<String> authorize(@RequestBody final UserDto userDto);

  @PostMapping("/registration")
  ResponseEntity<String> register(@RequestBody final UserDto userDto);
}
