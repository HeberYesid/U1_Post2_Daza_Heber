package com.example.inventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Inventory implements InventoryService {
    private final Map<String, InventoryItem> productsByKey = new HashMap<>();

    @Override
    public void addProduct(String name, BigDecimal price, int initialQuantity, String category) {
        validateName(name);
        validatePositivePrice(price);
        if (initialQuantity < 0) {
            throw new IllegalArgumentException("Initial quantity cannot be negative");
        }
        validateCategory(category);

        String key = normalize(name);
        if (productsByKey.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate product name");
        }

        productsByKey.put(key, new InventoryItem(name.trim(), price, initialQuantity, category.trim()));
    }

    @Override
    public void registerSale(String productName, int units) {
        InventoryItem product = getRequiredProduct(productName);
        if (units <= 0) {
            throw new IllegalArgumentException("Units must be greater than zero");
        }
        if (units > product.getQuantity()) {
            throw new IllegalStateException("Insufficient stock");
        }
        product.sell(units);
    }

    @Override
    public void restock(String productName, int units) {
        InventoryItem product = getRequiredProduct(productName);
        if (units <= 0) {
            throw new IllegalArgumentException("Units must be greater than zero");
        }
        product.restock(units);
    }

    @Override
    public int getStock(String productName) {
        return getRequiredProduct(productName).getQuantity();
    }

    @Override
    public List<Product> getProductsBelowStockThreshold(Integer threshold) {
        int effectiveThreshold = threshold == null ? 5 : threshold;
        if (effectiveThreshold < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }

        List<Product> result = new ArrayList<>();
        for (InventoryItem product : productsByKey.values()) {
            if (product.getQuantity() < effectiveThreshold) {
                result.add(product.toProduct());
            }
        }
        return result;
    }

    @Override
    public BigDecimal getTotalInventoryValue() {
        BigDecimal total = BigDecimal.ZERO;
        for (InventoryItem product : productsByKey.values()) {
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())));
        }
        return total;
    }

    @Override
    public int applyDiscountToCategory(String category, BigDecimal discountPercent) {
        validateCategory(category);
        validateDiscount(discountPercent);

        int affected = 0;
        String normalizedCategory = normalize(category);
        for (InventoryItem product : productsByKey.values()) {
            if (normalize(product.getCategory()).equals(normalizedCategory)) {
                product.applyDiscount(discountPercent);
                affected++;
            }
        }
        return affected;
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase();
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
    }

    private static void validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or blank");
        }
    }

    private static void validatePositivePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
    }

    private static void validateDiscount(BigDecimal discountPercent) {
        if (discountPercent == null
                || discountPercent.compareTo(BigDecimal.ZERO) < 0
                || discountPercent.compareTo(new BigDecimal("50")) > 0) {
            throw new IllegalArgumentException("Discount must be between 0 and 50");
        }
    }

    private InventoryItem getRequiredProduct(String productName) {
        validateName(productName);
        InventoryItem product = productsByKey.get(normalize(productName));
        if (product == null) {
            throw new NoSuchElementException("Product not found");
        }
        return product;
    }
}
