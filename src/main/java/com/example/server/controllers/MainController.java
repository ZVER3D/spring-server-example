package com.example.server.controllers;

import com.example.server.domain.Message;
import com.example.server.domain.User;
import com.example.server.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {
  @Value("${upload.path}")
  private String uploadPath;

  private final MessageRepo messageRepo;

  public MainController(MessageRepo messageRepo) {
    this.messageRepo = messageRepo;
  }

  @GetMapping
  public String index(@RequestParam(defaultValue = "") String filter, Model model) {
    Iterable<Message> messages;
    if (filter == null || filter.isEmpty()) {
      messages = messageRepo.findAll();
    } else {
      messages = messageRepo.findByTag(filter);
    }
    model.addAttribute("messages", messages);
    model.addAttribute("filter", filter);

    return "index";
  }

  @PostMapping
  public String postMessage(
      @AuthenticationPrincipal User user,
      @Valid Message message,
      @RequestParam MultipartFile file,
      BindingResult bindingResult,
      Model model)
      throws IOException {
    message.setAuthor(user);

    if (bindingResult.hasErrors()) {
      model.mergeAttributes(ControllerUtil.getErrors(bindingResult));
      model.addAttribute("message", message);

      return "redirect:/";
    }

    saveFile(message, file);

    messageRepo.save(message);
    model.addAttribute("message", null);

    return "redirect:/";
  }

  private void saveFile(Message message, MultipartFile file) throws IOException {
    if (file != null && !StringUtils.isEmpty(file.getOriginalFilename())) {
      File uploadDir = new File(uploadPath);
      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uidFile = UUID.randomUUID().toString();
      uidFile += "." + file.getOriginalFilename();
      file.transferTo(new File(uploadPath + "/" + uidFile));

      message.setFilename(uidFile);
    }
  }

  @GetMapping("/user-messages/{user}")
  public String userMessages(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      Model model,
      @RequestParam(required = false) Message message) {
    if (user == null) {
      return "redirect:/";
    }

    Set<Message> messages = user.getMessages();

    model.addAttribute("messages", messages);
    model.addAttribute("message", message);
    model.addAttribute("isCurrentUser", currentUser.equals(user));
    model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
    model.addAttribute("userChannel", user);
    model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
    model.addAttribute("subscribersCount", user.getSubscribers().size());

    return "userMessages";
  }

  @PostMapping("/user-messages/{user}")
  public String updateMessage(
      @AuthenticationPrincipal User currentUser,
      @PathVariable Long user,
      @RequestParam("id") Message message,
      @RequestParam("text") String text,
      @RequestParam("tag") String tag,
      @RequestParam("file") MultipartFile file)
      throws IOException {
    if (!message.getAuthor().equals(currentUser)) {
      return "redirect:/user-messages/" + currentUser.getId();
    }

    if (!StringUtils.isEmpty(text)) {
      message.setText(text);
    }

    if (!StringUtils.isEmpty(tag)) {
      message.setTag(tag);
    }

    saveFile(message, file);

    messageRepo.save(message);

    return "redirect:/user-messages/" + user;
  }
}
