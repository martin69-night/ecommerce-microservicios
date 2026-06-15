package cl.duoc.orderservice.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pedido - Capa Modelo")
class PedidoModelTest {

    @Test
    @DisplayName("Constructor vacío debe crear objeto no nulo")
    void constructorVacioDebeCrearObjetoNoNulo() {
        Pedido pedido = new Pedido();
        assertNotNull(pedido);
    }

    @Test
    @DisplayName("calcularTotal debe multiplicar precio por cantidad")
    void calcularTotalDebeMultiplicarPrecioPorCantidad() {
        // Given
        Pedido pedido = new Pedido();
        pedido.setPrecioUnitario(new BigDecimal("100.00"));
        pedido.setCantidad(3);
        // When
        pedido.calcularTotal();
        // Then
        assertEquals(new BigDecimal("300.00"), pedido.getTotal());
    }

    @Test
    @DisplayName("Setters deben modificar los campos correctamente")
    void settersDebenModificarLosCamposCorrectamente() {
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(1L);
        pedido.setProductoId(2L);
        pedido.setSku("SKU-001");
        pedido.setNombreProducto("Laptop");
        pedido.setEstado("CREADO");
        pedido.setActivo(true);
        assertEquals(1L, pedido.getUsuarioId());
        assertEquals("SKU-001", pedido.getSku());
        assertEquals("CREADO", pedido.getEstado());
        assertTrue(pedido.getActivo());
    }

    @Test
    @DisplayName("calcularTotal no debe fallar si precio o cantidad son nulos")
    void calcularTotalNoDebeFallarSiCamposSonNulos() {
        Pedido pedido = new Pedido();
        assertDoesNotThrow(pedido::calcularTotal);
    }
}
