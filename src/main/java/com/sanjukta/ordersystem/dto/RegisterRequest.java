package com.sanjukta.ordersystem.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterRequest(

        @NotNull(message = "Username is required for registration!")
        String username,

        @NotNull(message = "Email is required for registration!")
        String email,

        @NotNull(message = "Please select a password!")
        String password) {
}
