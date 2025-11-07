package com.example.bbs.controller;

import com.example.bbs.entity.User;
import com.example.bbs.security.JwtProvider;
import com.example.bbs.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        User user = userService.register(body.get("username"), body.get("password"));
        return ResponseEntity.ok(Map.of("message", "회원가입 완료", "userId", user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        User user = userService.authenticate(body.get("username"), body.get("password"));
        String token = jwtProvider.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
