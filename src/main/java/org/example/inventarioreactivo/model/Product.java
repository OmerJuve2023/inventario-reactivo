package org.example.inventarioreactivo.model;

import java.math.BigDecimal;

public record Product(Long id, String name, BigDecimal price, int stock) {
}
