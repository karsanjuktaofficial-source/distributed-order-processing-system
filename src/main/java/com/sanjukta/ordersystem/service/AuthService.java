package com.sanjukta.ordersystem.service;

import com.sanjukta.ordersystem.dto.AuthResponse;
import com.sanjukta.ordersystem.dto.LoginRequest;
import com.sanjukta.ordersystem.dto.RegisterRequest;
import com.sanjukta.ordersystem.entity.User;
import com.sanjukta.ordersystem.repository.UserRepository;
import com.sanjukta.ordersystem.security.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //register new user
    public String register(RegisterRequest request) {
        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        //only register new unique users
        try {
            userRepository.save(user);
        }
        catch (DataIntegrityViolationException e) {
            return "User already exists";
        }

        return "User registered successfully";
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {

        //check if user exists in database
        User user = userRepository.findByUsername(
                loginRequest.username()
        ).orElseThrow(()-> new RuntimeException("Username not found"));

        //encode given password then compare it with db
        boolean matches = passwordEncoder.matches(loginRequest.password(), user.getPassword());

        if (!matches) {
            throw new RuntimeException("Wrong password");
        }

        //generate token if credentials are valid
        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(token);
    }
}
