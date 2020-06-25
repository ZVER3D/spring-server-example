package com.example.server.controllers;

import com.example.server.domain.Role;
import com.example.server.domain.User;
import com.example.server.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public String userList(Model model) {
    model.addAttribute("users", userService.findAll());
    return "userList";
  }

  @GetMapping("{user}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public String userEditForm(@PathVariable User user, Model model) {
    model.addAttribute("user", user);
    model.addAttribute("roles", Role.values());
    return "userEdit";
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public String userSave(
      @RequestParam("userId") User user,
      @RequestParam String username,
      @RequestParam Map<String, String> form) {
    userService.saveUser(user, username, form);

    return "redirect:/user";
  }

  @GetMapping("profile")
  public String getProfile(@AuthenticationPrincipal User user, Model model) {
    model.addAttribute("email", user.getEmail());

    return "profile";
  }

  @PostMapping("profile")
  public String updateProfile(
      @AuthenticationPrincipal User user,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String email
  ) {
    userService.updateProfile(user, password, email);

    return "redirect:/user/profile";
  }

  @GetMapping("subscribe/{user}")
  public String subscribe(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
    userService.subscribe(currentUser, user);

    return "redirect:/user-messages/" + user.getId();
  }

  @GetMapping("unsubscribe/{user}")
  public String unsubscribe(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
    userService.unsubscribe(currentUser, user);

    return "redirect:/user-messages/" + user.getId();
  }

  @GetMapping("{type}/{user}/list")
  public String userList(@PathVariable String type, @PathVariable User user, Model model) {
    model.addAttribute("userChannel", user);
    model.addAttribute("type", type);

    if ("subscriptions".equals(type)) {
      model.addAttribute("users", user.getSubscriptions());
    } else {
      model.addAttribute("users", user.getSubscribers());
    }

    return "subscriptions";
  }
}
