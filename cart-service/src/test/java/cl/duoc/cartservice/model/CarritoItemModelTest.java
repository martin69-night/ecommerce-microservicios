package cl.duoc.cartservice.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CarritoItem - Capa Modelo")
class CarritoItemModelTest {

    @Test
    @DisplayName("Constructor vacío debe crear objeto no nulo")
    void constructorVacioDebeCrearObjetoNoNulo() {
        CarritoItem item = new CarritoItem();
        assertNotNull(item);
    }

    @Test
    @DisplayName("Constructor completo debe asignar todos los campos")
    void constructorCompletoDebeAsignarTodosLosCampos() {
        CarritoItem item = new CarritoItem(1L, 2L, 3L, "SKU-001", "Laptop",
                new BigDecimal("999.99"), 2, true, null);
        assertEquals(1L, item.getId());
        assertEquals("SKU-001", item.getSku());
        assertEquals("Laptop", item.getNombreProducto());
        assertEquals(2, item.getCantidad());
        assertTrue(item.getActivo());
    }

    @Test
    @DisplayName("getSubtotal debe retornar precio por cantidad")
    void getSubtotalDebeRetornarPrecioPorCantidad() {
        CarritoItem item = new CarritoItem();
        item.setPrecioUnitario(new BigDecimal("100.00"));
        item.setCantidad(3);
        assertEquals(new BigDecimal("300.00"), item.getSubtotal());
    }

    @Test
    @DisplayName("getSubtotal debe retornar cero si precio es nulo")
    void getSubtotalDebeRetornarCeroSiPrecioEsNulo() {
        CarritoItem item = new CarritoItem();
        assertEquals(BigDecimal.ZERO, item.getSubtotal());
    }
}
