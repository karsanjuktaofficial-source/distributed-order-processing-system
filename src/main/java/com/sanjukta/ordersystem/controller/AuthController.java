package com.sanjukta.ordersystem.controller;

import com.sanjukta.ordersystem.dto.AuthResponse;
import com.sanjukta.ordersystem.dto.LoginRequest;
import com.sanjukta.ordersystem.dto.RegisterRequest;
import com.sanjukta.ordersystem.entity.User;
import com.sanjukta.ordersystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }


}
