package com.example.server.controllers;

import com.example.server.domain.User;
import com.example.server.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
  private final UserService userService;

  public RegistrationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/registration")
  public String registration() {
    return "registration";
  }

  @PostMapping("/registration")
  public String register(User user, Model model) {
    if (!userService.addUser(user)) {
      model.addAttribute("message", "User already exists");
      return "registration";
    }

    model.addAttribute("message", "Check your email to activate your account");

    return "login";
  }

  @GetMapping("/activate/{code}")
  public String activate(@PathVariable String code, Model model) {
    boolean isActivated = userService.activateUser(code);
    if (isActivated) {
      model.addAttribute("message", "Your account successfully activated");
    } else {
      model.addAttribute("message", "Activation code is invalid");
    }

    return "login";
  }
}
