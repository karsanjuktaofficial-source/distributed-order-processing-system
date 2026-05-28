package com.sanjukta.ordersystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateProductRequest(
        @NotBlank(message = "Name of the product is required!")
        String name,
        String description,
        @NotNull
        @PositiveOrZero(message="Price cannot be lesser that zero!")
        BigDecimal price,
        @NotNull
        @PositiveOrZero(message="Quantity cannot be lesser that zero!")
        Integer availableQuantity

) {
}
