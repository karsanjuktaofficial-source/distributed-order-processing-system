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

        //get header from http request and check if bearer token is included, if not return
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        //get token and username from header
        String token = header.substring(7);
        String username = jwtService.extractUsername(token);

        //check if user exists
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Username not found"));

        //if token is valid, get auth token from spring security
        if (jwtService.isTokenValid(token, username)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username,
                            null,
                            Collections.emptyList());
            //set request details to token
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //set token to security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }


    //finally filter request, response
    chain.doFilter(request, response);
    }

}
