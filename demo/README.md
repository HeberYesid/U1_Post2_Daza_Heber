# Sistema de Inventario (TDD)

Proyecto Java (Maven) para gestionar inventario de productos, construido con enfoque TDD.

## Requisitos previos

- Java 17+
- Maven 3.9+ (o compatible)

## Instrucciones de compilación y ejecución

Desde la carpeta `demo`:

### 1) Compilar

```bash
mvn clean compile
```

### 2) Ejecutar pruebas

```bash
mvn test
```

### 3) Empaquetar artefacto

```bash
mvn clean package
```

## Estructura principal

- `src/main/java/com/example/inventory/Inventory.java`: implementación principal del servicio de inventario.
- `src/main/java/com/example/inventory/InventoryItem.java`: entidad interna mutable (stock/precio).
- `src/main/java/com/example/inventory/Product.java`: DTO inmutable de salida.
- `src/main/java/com/example/inventory/InventoryService.java`: contrato del servicio.
- `src/test/java/com/example/inventory/InventoryServiceEdgeCasesTest.java`: suite de pruebas de casos borde.
- `trazabilidad.md`: matriz requisito ↔ pruebas ↔ código.

## Supuestos

1. **Unicidad de nombre case-insensitive**: `"Arroz"` y `" arroz "` son el mismo producto.
2. **Normalización por `trim()`** en nombre/categoría para entradas con espacios.
3. **Umbral por defecto de stock bajo = 5** cuando se pasa `null`.
4. **Criterio de stock bajo estricto**: se incluye si `stock < umbral` (no `<=`).
5. **Reposición y venta requieren `N > 0`** (`0` y negativos son inválidos).
6. **Descuento válido en [0, 50]** inclusive.
7. **Categoría sin productos al descontar** devuelve `0` afectados (no error).
8. **Producto inexistente** en operaciones de stock/venta/restock lanza `NoSuchElementException`.
9. **Inventario vacío** en valor total retorna `0`.
10. **No persistencia**: almacenamiento en memoria (estado por instancia).

## Decisiones de diseño

- **Separación de modelos**:
  - `InventoryItem` (mutable) para lógica interna de negocio.
  - `Product` (record inmutable) para resultados externos y listas.
- **Servicio orientado por interfaz**:
  - `InventoryService` define el contrato.
  - `Inventory` implementa la lógica.
- **Validaciones centralizadas** en `Inventory`:
  - `validateName`, `validateCategory`, `validatePositivePrice`, `validateDiscount`.
- **Búsqueda eficiente por nombre normalizado**:
  - `Map<String, InventoryItem> productsByKey` con clave normalizada.
- **Errores de dominio explícitos**:
  - `IllegalArgumentException` para entradas inválidas.
  - `IllegalStateException` para negocio inválido (venta excede stock).
  - `NoSuchElementException` para producto inexistente.
- **Compatibilidad de pruebas TDD**:
  - `InventoryServicePlaceholder` extiende `Inventory` para mantener la suite existente.

## Flujo TDD aplicado

1. Pruebas de casos borde en rojo.
2. Implementación incremental mínima por operación.
3. Refactor de nombres a inglés completo.
4. Verificación final con toda la suite en verde.

## Estado actual

- Suite de pruebas: **verde** (`23/23` pasando).
- Cobertura funcional enfocada en requisitos y restricciones del enunciado.
