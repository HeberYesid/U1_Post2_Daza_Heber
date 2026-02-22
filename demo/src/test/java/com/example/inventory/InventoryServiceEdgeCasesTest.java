package com.example.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryServiceEdgeCasesTest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryServicePlaceholder();
    }

    @Test
    @DisplayName("Agregar: nombre nulo debe fallar")
    void addProduct_nullName_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct(null, new BigDecimal("10.00"), 3, "Lacteos"));
    }

    @Test
    @DisplayName("Agregar: nombre vacío debe fallar")
    void addProduct_blankName_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct("   ", new BigDecimal("10.00"), 3, "Lacteos"));
    }

    @Test
    @DisplayName("Agregar: precio cero debe fallar")
    void addProduct_zeroPrice_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct("Arroz", BigDecimal.ZERO, 3, "Granos"));
    }

    @Test
    @DisplayName("Agregar: precio negativo debe fallar")
    void addProduct_negativePrice_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct("Arroz", new BigDecimal("-1.00"), 3, "Granos"));
    }

    @Test
    @DisplayName("Agregar: cantidad inicial negativa debe fallar")
    void addProduct_negativeInitialQuantity_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct("Arroz", new BigDecimal("4.80"), -1, "Granos"));
    }

    @Test
    @DisplayName("Agregar: no permite duplicado case-insensitive")
    void addProduct_duplicateName_caseInsensitive_shouldFail() {
        service.addProduct("Arroz", new BigDecimal("4.80"), 10, "Granos");
        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct(" arroz ", new BigDecimal("5.10"), 4, "Granos"));
    }

    @Test
    @DisplayName("Venta: producto inexistente debe fallar")
    void registerSale_nonExistingProduct_shouldFail() {
        assertThrows(NoSuchElementException.class, () -> service.registerSale("Cafe", 1));
    }

    @Test
    @DisplayName("Venta: unidades cero debe fallar")
    void registerSale_zeroUnits_shouldFail() {
        service.addProduct("Cafe", new BigDecimal("7.90"), 10, "Bebidas");
        assertThrows(IllegalArgumentException.class, () -> service.registerSale("Cafe", 0));
    }

    @Test
    @DisplayName("Venta: unidades negativas debe fallar")
    void registerSale_negativeUnits_shouldFail() {
        service.addProduct("Cafe", new BigDecimal("7.90"), 10, "Bebidas");
        assertThrows(IllegalArgumentException.class, () -> service.registerSale("Cafe", -2));
    }

    @Test
    @DisplayName("Venta: vender exactamente el stock debe dejar 0")
    void registerSale_exactStock_shouldSetStockToZero() {
        service.addProduct("Cafe", new BigDecimal("7.90"), 10, "Bebidas");
        service.registerSale("Cafe", 10);
        assertEquals(0, service.getStock("Cafe"));
    }

    @Test
    @DisplayName("Venta: exceder stock debe fallar")
    void registerSale_unitsExceedStock_shouldFail() {
        service.addProduct("Cafe", new BigDecimal("7.90"), 10, "Bebidas");
        assertThrows(IllegalStateException.class, () -> service.registerSale("Cafe", 11));
    }

    @Test
    @DisplayName("Reposición: producto inexistente debe fallar")
    void restock_nonExistingProduct_shouldFail() {
        assertThrows(NoSuchElementException.class, () -> service.restock("Cafe", 5));
    }

    @Test
    @DisplayName("Reposición: unidades cero debe fallar")
    void restock_zeroUnits_shouldFail() {
        service.addProduct("Leche", new BigDecimal("3.40"), 1, "Lacteos");
        assertThrows(IllegalArgumentException.class, () -> service.restock("Leche", 0));
    }

    @Test
    @DisplayName("Reposición: unidades negativas debe fallar")
    void restock_negativeUnits_shouldFail() {
        service.addProduct("Leche", new BigDecimal("3.40"), 1, "Lacteos");
        assertThrows(IllegalArgumentException.class, () -> service.restock("Leche", -1));
    }

    @Test
    @DisplayName("Consultar stock: producto inexistente debe fallar")
    void getStock_nonExistingProduct_shouldFail() {
        assertThrows(NoSuchElementException.class, () -> service.getStock("Te"));
    }

    @Test
    @DisplayName("Stock bajo: umbral por defecto 5")
    void getProductsBelowStockThreshold_defaultThreshold_shouldReturnOnlyLessThanFive() {
        service.addProduct("A", new BigDecimal("1.00"), 4, "X");
        service.addProduct("B", new BigDecimal("1.00"), 5, "X");
        service.addProduct("C", new BigDecimal("1.00"), 6, "X");
        assertEquals(1, service.getProductsBelowStockThreshold(null).size());
    }

    @Test
    @DisplayName("Stock bajo: umbral negativo debe fallar")
    void getProductsBelowStockThreshold_negativeThreshold_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getProductsBelowStockThreshold(-1));
    }

    @Test
    @DisplayName("Valor total: inventario vacío debe dar 0")
    void getTotalInventoryValue_emptyInventory_shouldBeZero() {
        assertEquals(BigDecimal.ZERO, service.getTotalInventoryValue());
    }

    @Test
    @DisplayName("Descuento: 0% y 50% son válidos")
    void applyDiscountToCategory_boundaries_shouldBeValid() {
        service.addProduct("Leche", new BigDecimal("10.00"), 1, "Lacteos");
        assertEquals(1, service.applyDiscountToCategory("Lacteos", BigDecimal.ZERO));
        assertEquals(1, service.applyDiscountToCategory("Lacteos", new BigDecimal("50")));
    }

    @Test
    @DisplayName("Descuento: porcentaje mayor a 50% debe fallar")
    void applyDiscountToCategory_greaterThanFifty_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.applyDiscountToCategory("Lacteos", new BigDecimal("50.01")));
    }

    @Test
    @DisplayName("Descuento: porcentaje negativo debe fallar")
    void applyDiscountToCategory_negative_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.applyDiscountToCategory("Lacteos", new BigDecimal("-1")));
    }

    @Test
    @DisplayName("Descuento: categoría inexistente retorna 0 afectados")
    void applyDiscountToCategory_nonExistingCategory_shouldAffectZero() {
        service.addProduct("Leche", new BigDecimal("10.00"), 1, "Lacteos");
        assertEquals(0, service.applyDiscountToCategory("Electro", new BigDecimal("10")));
    }
}
