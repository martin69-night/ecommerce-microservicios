package cl.duoc.inventoryservice.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Inventario - Capa Modelo")
class InventarioModelTest {

    @Test
    @DisplayName("Constructor vacío debe crear objeto no nulo")
    void constructorVacioDebeCrearObjetoNoNulo() {
        Inventario inv = new Inventario();
        assertNotNull(inv);
    }

    @Test
    @DisplayName("Constructor completo debe asignar todos los campos")
    void constructorCompletoDebeAsignarTodosLosCampos() {
        Inventario inv = new Inventario(1L, 10L, "SKU-001", 100, 20, "Bodega A", true);
        assertEquals(1L, inv.getId());
        assertEquals("SKU-001", inv.getSku());
        assertEquals(100, inv.getCantidadDisponible());
        assertEquals(20, inv.getCantidadReservada());
        assertTrue(inv.getActivo());
    }

    @Test
    @DisplayName("getStockLibre debe retornar disponible menos reservada")
    void getStockLibreDebeRetornarDiferenciaCorrecta() {
        Inventario inv = new Inventario(1L, 10L, "SKU-001", 100, 20, "Bodega A", true);
        assertEquals(80, inv.getStockLibre());
    }

    @Test
    @DisplayName("Setters deben modificar los campos correctamente")
    void settersDebenModificarLosCamposCorrectamente() {
        Inventario inv = new Inventario();
        inv.setSku("SKU-002");
        inv.setCantidadDisponible(50);
        inv.setActivo(false);
        assertEquals("SKU-002", inv.getSku());
        assertEquals(50, inv.getCantidadDisponible());
        assertFalse(inv.getActivo());
    }
}
