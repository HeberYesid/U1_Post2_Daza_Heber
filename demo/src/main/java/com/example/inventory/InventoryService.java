package com.example.inventory;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryService {

    void addProduct(String name, BigDecimal price, int initialQuantity, String category);

    void registerSale(String productName, int units);

    void restock(String productName, int units);

    int getStock(String productName);

    List<Product> getProductsBelowStockThreshold(Integer threshold);

    BigDecimal getTotalInventoryValue();

    int applyDiscountToCategory(String category, BigDecimal discountPercent);
}
