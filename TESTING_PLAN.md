# Plan de Pruebas Unitarias — Proyecto E-Commerce Microservicios
## DSY1103 — Desarrollo Fullstack I

---

## Reglas de Negocio Críticas por Microservicio

### catalog-service
1. **SKU único:** No se puede crear un producto con un SKU ya existente.
2. **Producto activo:** Solo los productos activos son retornados en el listado principal.
3. **Precio válido:** El precio de un producto debe ser mayor a cero.

### inventory-service
1. **SKU único:** No se puede crear un inventario con un SKU ya existente.
2. **Stock libre suficiente:** No se puede reservar más stock del disponible libre (disponible - reservado).
3. **Cantidad válida:** Toda operación de stock requiere una cantidad mayor a cero.

### order-service
1. **Stock suficiente:** No se puede crear un pedido si el stock libre en inventory-service es insuficiente.
2. **Pedido no cancelable:** No se puede cancelar un pedido que ya está en estado PAGADO.
3. **Campos obligatorios:** usuarioId, productoId, SKU, nombre, precio y cantidad son obligatorios al crear un pedido.

### payment-service
1. **Solo pendientes aprobables:** Solo se pueden aprobar pagos en estado PENDIENTE.
2. **Solo pendientes rechazables:** Solo se pueden rechazar pagos en estado PENDIENTE.
3. **Pago aprobado no anulable:** No se puede anular un pago que ya fue aprobado.
4. **Monto válido:** El monto del pago debe ser mayor a cero.

### cart-service
1. **Carrito no vacío:** No se puede vaciar un carrito que no tiene items activos.
2. **Cantidad válida:** No se puede agregar un item con cantidad menor o igual a cero.
3. **Subtotal correcto:** El subtotal de un item es precio unitario multiplicado por cantidad.

---

## Cobertura Actual

### catalog-service
| Regla | Estado | Casos cubiertos |
|---|---|---|
| SKU único | ✅ Cubierta | crearDebeLanzarExcepcionSiSkuYaExiste |
| Producto activo | ✅ Cubierta | listarActivosDebeRetornarListaDeProductosActivos |
| Precio válido | ✅ Cubierta | buscarPorIdDebeLanzarExcepcionCuandoNoExiste |

### inventory-service
| Regla | Estado | Casos cubiertos |
|---|---|---|
| SKU único | ✅ Cubierta | crearDebeLanzarExcepcionSiSkuYaExiste |
| Stock libre suficiente | ✅ Cubierta | reservarStockDebeLanzarExcepcionSiStockInsuficiente |
| Cantidad válida | ✅ Cubierta | reservarStockDebeLanzarExcepcionSiCantidadEsCero |

### order-service
| Regla | Estado | Casos cubiertos |
|---|---|---|
| Stock suficiente | ✅ Cubierta | crearDebeLanzarExcepcionSiStockInsuficiente |
| Pedido no cancelable | ✅ Cubierta | cancelarDebeLanzarExcepcionSiPedidoYaEstaPagado |
| Campos obligatorios | ✅ Cubierta | validarPedido via crearDebeLanzarExcepcionSiStockInsuficiente |

### payment-service
| Regla | Estado | Casos cubiertos |
|---|---|---|
| Solo pendientes aprobables | ✅ Cubierta | aprobarDebeLanzarExcepcionSiNoEstaPendiente |
| Pago aprobado no anulable | ✅ Cubierta | anularDebeLanzarExcepcionSiPagoEstaAprobado |
| Monto válido | ✅ Cubierta | crearDebeLanzarExcepcionSiMontoEsCero |
| Solo pendientes rechazables | ⚠️ Pendiente | Solo caso feliz cubierto |

### cart-service
| Regla | Estado | Casos cubiertos |
|---|---|---|
| Carrito no vacío | ✅ Cubierta | vaciarCarritoDebeLanzarExcepcionSiCarritoVacio |
| Cantidad válida | ✅ Cubierta | agregarItemDebeLanzarExcepcionSiCantidadEsCero |
| Subtotal correcto | ✅ Cubierta | getSubtotalDebeRetornarPrecioPorCantidad |

---

## Tabla de Cobertura General

| Microservicio | Modelo | Servicio | Controller | Repositorio | Total |
|---|---|---|---|---|---|
| catalog-service | 4 | 5 | 4 | 3 | **16** |
| inventory-service | 4 | 6 | 4 | 3 | **17** |
| order-service | 4 | 5 | 4 | 3 | **16** |
| payment-service | 4 | 6 | 4 | 3 | **17** |
| cart-service | 4 | 6 | 4 | 3 | **17** |
| **TOTAL** | **20** | **28** | **20** | **15** | **83** |

---

## Reflexión y Deuda Técnica

- **Riesgo sin probar:** La regla de rechazo de pagos pendientes en payment-service no tiene test de caso de error explícito.
- **Acción futura:** Agregar test `rechazarDebeLanzarExcepcionSiNoEstaPendiente` en PagoServiceTest.
- **Responsable:** Equipo Backend · Sprint final
- **Frameworks utilizados:** JUnit 5, Mockito, MockMvc, DataJpaTest (Spring Boot 4.x)
- **Estado general:** BUILD SUCCESS en los 5 microservicios ✅
