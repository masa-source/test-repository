package com.example.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.model.SiteUser;
import com.example.demo.repository.SiteUserRepository;
import com.example.demo.util.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SiteUserService {
	private final SiteUserRepository userRepository;

	public SiteUser save(SiteUser user) {
		return userRepository.save(user);
	}

	public SiteUser tmpSiteUser() {
		SiteUser user = new SiteUser();
		user.setUsername("瓜田");
		user.setPassword("password");
		user.setEmail("urita@example.com");
		user.setRole(Role.USER.name());
		return user;
	}

	public SiteUser getLoginUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findByUsername(auth.getName());
	}
}
