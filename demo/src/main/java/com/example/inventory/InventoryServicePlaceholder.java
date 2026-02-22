package com.example.inventory;

import java.math.BigDecimal;
import java.util.List;

public class InventoryServicePlaceholder implements InventoryService {

    @Override
    public void addProduct(String name, BigDecimal price, int initialQuantity, String category) {
        throw new UnsupportedOperationException("TDD RED: not implemented yet");
    }

    @Override
    public void registerSale(String productName, int units) {
        throw new UnsupportedOperationException("TDD RED: not implemented yet");
    }

    @Override
    public void restock(String productName, int units) {
        throw new UnsupportedOperationException("TDD RED: not implemented yet");
    }

    @Override
    public int getStock(String productName) {
        throw new UnsupportedOperationException("TDD RED: not implemented yet");
    }

    @Override
    public List<Product> getProductsBelowStockThreshold(Integer threshold) {
        throw new UnsupportedOperationException("TDD RED: not implemented yet");
    }

    @Override
    public BigDecimal getTotalInventoryValue() {
        throw new UnsupportedOperationException("TDD RED: not implemented yet");
    }

    @Override
    public int applyDiscountToCategory(String category, BigDecimal discountPercent) {
        throw new UnsupportedOperationException("TDD RED: not implemented yet");
    }
}
