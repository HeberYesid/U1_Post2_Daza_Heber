package com.example.inventory;

import java.math.BigDecimal;

public class InventoryItem {
    private final String name;
    private BigDecimal price;
    private int quantity;
    private final String category;

    public InventoryItem(String name, BigDecimal price, int quantity, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public void sell(int units) {
        quantity -= units;
    }

    public void restock(int units) {
        quantity = Math.addExact(quantity, units);
    }

    public void applyDiscount(BigDecimal discountPercent) {
        BigDecimal factor = BigDecimal.ONE.subtract(discountPercent.divide(new BigDecimal("100")));
        price = price.multiply(factor);
    }

    public Product toProduct() {
        return new Product(name, price, quantity, category);
    }
}
