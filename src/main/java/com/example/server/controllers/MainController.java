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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
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

      return "index";
    }

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

    messageRepo.save(message);
    model.addAttribute("message", null);

    return "index";
  }
}
