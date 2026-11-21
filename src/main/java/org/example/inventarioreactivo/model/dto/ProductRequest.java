package org.example.inventarioreactivo.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "name is obligatory")
        String name,
        @NotNull(message = "price is obligatory")
        @DecimalMin(value = "0", inclusive = true)
        BigDecimal price,
        @Min(value = 0, message = "stock must be >=0")
        int stock
) {
}
