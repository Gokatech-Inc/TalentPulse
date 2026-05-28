package com.gokatech.talentpulse.controller;

import com.gokatech.talentpulse.dto.AuthRequest;
import com.gokatech.talentpulse.dto.AuthResponse;
import com.gokatech.talentpulse.model.User;
import com.gokatech.talentpulse.repository.UserRepository;
import com.gokatech.talentpulse.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .role(req.getRole() != null ? req.getRole() : User.UserRole.EMPLOYEE)
                .build();
        userRepository.save(user);
        return ResponseEntity.status(201).body("Registered: " + req.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        return userRepository.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .map(u -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(u.getEmail(), u.getRole().name()), "bearer")))
                .orElse(ResponseEntity.status(401).build());
    }
}
