package com.sanjukta.ordersystem.dto;

public record LoginRequest(
        String username,
        String password
) {
}