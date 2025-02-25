package com.heisyenberg.ssrfrontend.controllers;

import com.heisyenberg.ssrfrontend.dtos.User;
import com.heisyenberg.ssrfrontend.services.clients.UsersServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersController {
  private final UsersServiceClient usersServiceClient;

  @GetMapping({"/", "/users"})
  public String getUsers(
      @RequestParam(required = false) final Integer limit,
      @RequestParam(required = false) final Integer skip,
      final Model model) {
    Page<User> users = usersServiceClient.getUsers(limit, skip).getBody();
    model.addAttribute("users", users);
    return "users";
  }
}
