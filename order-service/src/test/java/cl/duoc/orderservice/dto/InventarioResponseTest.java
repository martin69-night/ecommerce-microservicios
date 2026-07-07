package cl.duoc.orderservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InventarioResponse - DTO de Inventory Service")
class InventarioResponseTest {

    @Test
    void constructorCompletoDebeAsignarTodosLosCampos() {
        InventarioResponse inventario = new InventarioResponse(
                1L,
                100L,
                "SKU-001",
                20,
                3,
                17,
                "Bodega A",
                true
        );

        assertEquals(1L, inventario.getId());
        assertEquals(100L, inventario.getProductoId());
        assertEquals("SKU-001", inventario.getSku());
        assertEquals(20, inventario.getCantidadDisponible());
        assertEquals(3, inventario.getCantidadReservada());
        assertEquals(17, inventario.getStockLibre());
        assertEquals("Bodega A", inventario.getUbicacion());
        assertTrue(inventario.getActivo());
    }

    @Test
    void settersDebenModificarTodosLosCampos() {
        InventarioResponse inventario = new InventarioResponse();

        inventario.setId(2L);
        inventario.setProductoId(200L);
        inventario.setSku("SKU-002");
        inventario.setCantidadDisponible(30);
        inventario.setCantidadReservada(4);
        inventario.setStockLibre(26);
        inventario.setUbicacion("Bodega B");
        inventario.setActivo(false);

        assertEquals(2L, inventario.getId());
        assertEquals(200L, inventario.getProductoId());
        assertEquals("SKU-002", inventario.getSku());
        assertEquals(30, inventario.getCantidadDisponible());
        assertEquals(4, inventario.getCantidadReservada());
        assertEquals(26, inventario.getStockLibre());
        assertEquals("Bodega B", inventario.getUbicacion());
        assertFalse(inventario.getActivo());
    }
}
