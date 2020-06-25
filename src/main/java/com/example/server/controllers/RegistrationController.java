package com.example.server.controllers;

import com.example.server.domain.User;
import com.example.server.domain.dto.CaptchaResponseDto;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
public class RegistrationController {
  private static final String CAPTCHA_URL = "https://hcaptcha.com/siteverify";

  private final UserService userService;
  private final RestTemplate restTemplate;

  public RegistrationController(UserService userService, RestTemplate restTemplate) {
    this.userService = userService;
    this.restTemplate = restTemplate;
  }

  @Value("${hcaptcha.secret}")
  private String secret;

  @GetMapping("/registration")
  public String registration() {
    return "registration";
  }

  @PostMapping("/registration")
  public String register(
      @RequestParam("h-captcha-response") String captchaResponse,
      @Valid User user,
      BindingResult bindingResult,
      Model model) {
    model.addAttribute("user", user);

    HashMap<String, String> postRequest = new HashMap<>();
    postRequest.put("response", captchaResponse);
    postRequest.put("secret", secret);
    try {
      CaptchaResponseDto response =
          restTemplate.postForObject(CAPTCHA_URL, postRequest, CaptchaResponseDto.class);
      if (response == null || !response.isSuccess()) {
        model.addAttribute("captchaError", "Fill captcha");
        return "registration";
      }
    } catch (RestClientException e) {
      model.addAttribute("captchaError", "Fill captcha");
      return "registration";
    }

    if (bindingResult.hasErrors()) {
      model.mergeAttributes(ControllerUtil.getErrors(bindingResult));
      return "registration";
    }

    if (!userService.addUser(user)) {
      model.addAttribute("usernameError", "User already exists");
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
