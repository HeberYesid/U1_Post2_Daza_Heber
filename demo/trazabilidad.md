# Matriz de Trazabilidad

Este documento conecta cada requisito funcional del enunciado con sus pruebas automáticas y con el código que lo implementa.

## Requisitos funcionales (R1-R7)

| ID | Requisito | Prueba(s) | Código que implementa | Estado |
|---|---|---|---|---|
| R1 | Agregar producto (nombre, precio, cantidad inicial, categoría) | `addProduct_nullName_shouldFail`, `addProduct_blankName_shouldFail`, `addProduct_zeroPrice_shouldFail`, `addProduct_negativePrice_shouldFail`, `addProduct_negativeInitialQuantity_shouldFail`, `addProduct_duplicateName_caseInsensitive_shouldFail` | `Inventory.addProduct(...)`, `Inventory.validateName(...)`, `Inventory.validatePositivePrice(...)`, `Inventory.validateCategory(...)`, `Inventory.normalize(...)` | ✅ Cubierto |
| R2 | Registrar venta de N unidades | `registerSale_nonExistingProduct_shouldFail`, `registerSale_zeroUnits_shouldFail`, `registerSale_negativeUnits_shouldFail`, `registerSale_exactStock_shouldSetStockToZero`, `registerSale_unitsExceedStock_shouldFail` | `Inventory.registerSale(...)`, `Inventory.getRequiredProduct(...)`, `InventoryItem.sell(...)` | ✅ Cubierto |
| R3 | Registrar reposición (restock) de N unidades | `restock_nonExistingProduct_shouldFail`, `restock_zeroUnits_shouldFail`, `restock_negativeUnits_shouldFail` | `Inventory.restock(...)`, `Inventory.getRequiredProduct(...)`, `InventoryItem.restock(...)` | ✅ Cubierto |
| R4 | Consultar stock actual de un producto | `getStock_nonExistingProduct_shouldFail`, `registerSale_exactStock_shouldSetStockToZero` (verifica lectura de stock luego de venta) | `Inventory.getStock(...)`, `Inventory.getRequiredProduct(...)` | ✅ Cubierto |
| R5 | Obtener productos con stock bajo umbral mínimo (default 5) | `getProductsBelowStockThreshold_defaultThreshold_shouldReturnOnlyLessThanFive`, `getProductsBelowStockThreshold_negativeThreshold_shouldFail` | `Inventory.getProductsBelowStockThreshold(...)` | ✅ Cubierto |
| R6 | Calcular valor total del inventario | `getTotalInventoryValue_emptyInventory_shouldBeZero` | `Inventory.getTotalInventoryValue(...)` | ✅ Cubierto |
| R7 | Aplicar descuento porcentual por categoría | `applyDiscountToCategory_boundaries_shouldBeValid`, `applyDiscountToCategory_greaterThanFifty_shouldFail`, `applyDiscountToCategory_negative_shouldFail`, `applyDiscountToCategory_nonExistingCategory_shouldAffectZero` | `Inventory.applyDiscountToCategory(...)`, `Inventory.validateDiscount(...)`, `InventoryItem.applyDiscount(...)` | ✅ Cubierto |

## Restricciones adicionales del cliente

| ID | Restricción | Prueba(s) | Código que implementa | Estado |
|---|---|---|---|---|
| C1 | Precios nunca negativos ni cero | `addProduct_zeroPrice_shouldFail`, `addProduct_negativePrice_shouldFail` | `Inventory.validatePositivePrice(...)` | ✅ Cubierto |
| C2 | Cantidades siempre enteros no negativos | `addProduct_negativeInitialQuantity_shouldFail`, `registerSale_zeroUnits_shouldFail`, `registerSale_negativeUnits_shouldFail`, `restock_zeroUnits_shouldFail`, `restock_negativeUnits_shouldFail` | `Inventory.addProduct(...)`, `Inventory.registerSale(...)`, `Inventory.restock(...)` | ✅ Cubierto |
| C3 | No pueden existir dos productos con mismo nombre | `addProduct_duplicateName_caseInsensitive_shouldFail` | `Inventory.addProduct(...)` + `Inventory.normalize(...)` + mapa `productsByKey` | ✅ Cubierto |
| C4 | Ventas no pueden exceder stock disponible | `registerSale_unitsExceedStock_shouldFail` | `Inventory.registerSale(...)` | ✅ Cubierto |
| C5 | Descuentos entre 0% y 50% (inclusive) | `applyDiscountToCategory_boundaries_shouldBeValid`, `applyDiscountToCategory_greaterThanFifty_shouldFail`, `applyDiscountToCategory_negative_shouldFail` | `Inventory.validateDiscount(...)`, `Inventory.applyDiscountToCategory(...)` | ✅ Cubierto |

## Ubicación de pruebas y código

- Pruebas: `src/test/java/com/example/inventory/InventoryServiceEdgeCasesTest.java`
- Implementación principal: `src/main/java/com/example/inventory/Inventory.java`
- Entidad mutable interna: `src/main/java/com/example/inventory/InventoryItem.java`
- DTO inmutable de salida: `src/main/java/com/example/inventory/Product.java`
- Clase usada por tests: `src/main/java/com/example/inventory/InventoryServicePlaceholder.java`
