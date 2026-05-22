package com.sanjukta.ordersystem.security;

import com.sanjukta.ordersystem.entity.User;
import com.sanjukta.ordersystem.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // 1. Get header from http request and check if bearer token is included
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Get token and username from header
        String token = header.substring(7);
        String username = jwtService.extractUsername(token);

        // 3. Check if user exists and context is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Safer approach: Use optional ifPresent/orElse instead of throwing a generic crash exception mid-filter
            User user = userRepository.findByUsername(username).orElse(null);

            // 4. If token and user are valid, set spring security context
            if (user != null && jwtService.isTokenValid(token, username)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.emptyList() // Pass user.getAuthorities() here if you have roles
                        );

                // Set request details to token.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set token to security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. Finally pass down the filter chain (MUST be inside the doFilterInternal method)
        chain.doFilter(request, response);
    }
}
