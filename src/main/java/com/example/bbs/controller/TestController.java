package com.example.bbs.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/me")
    public String currentUser(@AuthenticationPrincipal UserDetails user) {
        if (user == null) return "인증되지 않은 사용자";
        return "현재 로그인한 사용자: " + user.getUsername();
    }
}