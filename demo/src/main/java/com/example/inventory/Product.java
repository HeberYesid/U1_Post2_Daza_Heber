package com.example.inventory;

import java.math.BigDecimal;

public record Product(String name, BigDecimal price, int quantity, String category) {
}
