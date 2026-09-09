package com.neuroforge.neuroforge_backend.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neuroforge.neuroforge_backend.repository.UserRepository;

@RestController
public class SessionController {
    private final UserRepository userRepository;

    public SessionController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/session")
    public Map<String, Object> currentSession(Authentication authentication) {
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return Map.of(
            "email", authentication.getName(),
            "name", user.getName(),
            "organization", "Neuroforge",
            "roles", authentication.getAuthorities().stream().map(authority -> authority.getAuthority().replace("ROLE_", "")).toList(),
            "authenticated", true
        );
    }
}