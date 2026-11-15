package org.example.inventarioreactivo.model.dto;

import java.math.BigDecimal;

public record ProductRequest(String name, BigDecimal price, int stock) {
}
