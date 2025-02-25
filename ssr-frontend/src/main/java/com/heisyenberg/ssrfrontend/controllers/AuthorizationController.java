package com.heisyenberg.ssrfrontend.controllers;

import com.heisyenberg.ssrfrontend.dtos.UserDto;
import com.heisyenberg.ssrfrontend.services.clients.AuthorizationServiceClient;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationController {
  private final AuthorizationServiceClient authorizationServiceClient;

  @GetMapping("/authorization")
  public String authorization() {
    return "authorization";
  }

  @GetMapping("/registration")
  public String registrationPage(@ModelAttribute("userDto") UserDto userDto) {
    return "registration";
  }

  @PostMapping("/registration")
  public String registration(@Valid @ModelAttribute("userDto") UserDto userDto, final Model model) {
    try {
      authorizationServiceClient.register(userDto);
    } catch (FeignException e) {
      model.addAttribute("error", "Пользователь с таким именем уже зарегистрирован");
      return "registration";
    }
    return "redirect:/users";
  }
}
