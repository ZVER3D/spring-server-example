package com.example.server.controllers;

import com.example.server.domain.Message;
import com.example.server.domain.User;
import com.example.server.domain.dto.MessageDto;
import com.example.server.repos.MessageRepo;
import com.example.server.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
  @Value("${upload.path}")
  private String uploadPath;

  private final MessageRepo messageRepo;
  private final MessageService messageService;

  public MessageController(MessageRepo messageRepo, MessageService messageService) {
    this.messageRepo = messageRepo;
    this.messageService = messageService;
  }

  @GetMapping
  public String index(
      @AuthenticationPrincipal User user,
      @RequestParam(defaultValue = "") String filter,
      @PageableDefault(
              sort = {"id"},
              direction = Sort.Direction.DESC)
          Pageable pageable,
      Model model) {
    Page<MessageDto> page = messageService.messageList(user, filter, pageable);

    model.addAttribute("page", page);
    model.addAttribute("url", "/");
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

  @GetMapping("/user-messages/{author}")
  public String userMessages(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User author,
      @RequestParam(required = false) Message message,
      @PageableDefault(
              sort = {"id"},
              direction = Sort.Direction.DESC)
          Pageable pageable,
      Model model) {
    if (author == null) {
      return "redirect:/";
    }

    Page<MessageDto> page = messageService.messageListForUser(currentUser, author, pageable);

    model.addAttribute("page", page);
    model.addAttribute("message", message);
    model.addAttribute("isCurrentUser", currentUser.equals(author));
    model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
    model.addAttribute("userChannel", author);
    model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
    model.addAttribute("subscribersCount", author.getSubscribers().size());
    model.addAttribute("url", "/user-messages/" + author.getId());

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

  @GetMapping("/messages/{message}/like")
  public String like(
      @AuthenticationPrincipal User user,
      @PathVariable Message message,
      RedirectAttributes redirectAttributes,
      @RequestHeader(required = false) String referer) {
    Set<User> likes = message.getLikes();

    if (likes.contains(user)) {
      likes.remove(user);
    } else {
      likes.add(user);
    }

    messageRepo.save(message);

    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
    components.getQueryParams().forEach(redirectAttributes::addAttribute);

    return "redirect:" + components.getPath();
  }
}
