package cl.duoc.paymentservice.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pago - Capa Modelo")
class PagoModelTest {

    @Test
    @DisplayName("Constructor vacío debe crear objeto no nulo")
    void constructorVacioDebeCrearObjetoNoNulo() {
        Pago pago = new Pago();
        assertNotNull(pago);
    }

    @Test
    @DisplayName("Campos públicos deben asignarse correctamente")
    void camposPublicosDebenAsignarse() {
        // Given / When
        Pago pago = new Pago();
        pago.pedidoId = 1L;
        pago.usuarioId = 2L;
        pago.monto = new BigDecimal("150.00");
        pago.metodoPago = "TARJETA";
        pago.estado = "PENDIENTE";
        pago.activo = true;
        // Then
        assertEquals(1L, pago.pedidoId);
        assertEquals(2L, pago.usuarioId);
        assertEquals(new BigDecimal("150.00"), pago.monto);
        assertEquals("TARJETA", pago.metodoPago);
        assertEquals("PENDIENTE", pago.estado);
        assertTrue(pago.activo);
    }

    @Test
    @DisplayName("Activo debe ser true por defecto")
    void activoDebeSerTruePorDefecto() {
        Pago pago = new Pago();
        assertTrue(pago.activo);
    }

    @Test
    @DisplayName("Estado puede cambiar a APROBADO")
    void estadoPuedeCambiarAAprobado() {
        Pago pago = new Pago();
        pago.estado = "APROBADO";
        assertEquals("APROBADO", pago.estado);
    }
}
