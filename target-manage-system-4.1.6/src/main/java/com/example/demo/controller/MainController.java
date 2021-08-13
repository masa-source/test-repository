package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.SiteUser;
import com.example.demo.model.Target;
import com.example.demo.repository.SiteUserRepository;
import com.example.demo.repository.TargetRepository;
import com.example.demo.util.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	private final SiteUserRepository userRepository;
	private final TargetRepository targetRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String showList(Authentication loginUser, Model model) {
		model.addAttribute("username", loginUser.getName());
		model.addAttribute("role", loginUser.getAuthorities());
		return "user";
	}

	//    ログイン関連
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register(@ModelAttribute("user") SiteUser user) {
		return "register";
	}

	@PostMapping("/register")
	public String process(@Validated @ModelAttribute("user") SiteUser user,
			BindingResult result) {

		if (result.hasErrors()) {
			return "register";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (user.isAdmin()) {
			user.setRole(Role.ADMIN.name());
		} else {
			user.setRole(Role.USER.name());
		}
		userRepository.save(user);

		Target target = new Target();
		target.setName("TOP");
		target.setTop(true);
		target.setSiteuser(userRepository.findByUsername(user.getUsername()));
		LocalDateTime nowDateTime = LocalDateTime.now();
		target.setDateTime(nowDateTime);
		targetRepository.save(target);

		return "redirect:/login?register";
	}

	//    機能・ユーザ一覧
	@GetMapping("/admin/list")
	public String showAdminList(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "list";
	}

	// 機能・その他
	@GetMapping("/target")
	public String showtargetList(Authentication loginUser, Model model) {
		SiteUser user = userRepository.findByUsername(loginUser.getName());
		model.addAttribute("targets", targetRepository.findBySiteuser(user));
		return "target";
	}

}
