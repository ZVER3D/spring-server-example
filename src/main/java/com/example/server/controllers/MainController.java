package com.example.server.controllers;

import java.util.Map;

import com.example.server.domain.Message;
import com.example.server.domain.User;
import com.example.server.repos.MessageRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
	@Autowired
	private MessageRepo messageRepo;

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
	public String postMessage(@AuthenticationPrincipal User user, @RequestParam String text, @RequestParam String tag,
			Map<String, Object> model) {
		Message message = new Message(text, tag, user);
		messageRepo.save(message);

		return "redirect:/";
	}
}
