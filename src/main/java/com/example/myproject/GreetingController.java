package com.example.myproject;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
	@GetMapping("/greeting")
	public String greeting(@RequestParam(name = "name", defaultValue = "World") String name, Map<String, Object> map) {
		map.put("name", name);
		return "greeting";
	}
}
