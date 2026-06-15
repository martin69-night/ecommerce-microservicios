package cl.duoc.paymentservice.repository;

import cl.duoc.paymentservice.model.Pago;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("PagoRepository - Capa Repositorio")
class PagoRepositoryTest {

    @Autowired
    private PagoRepository pagoRepository;

    @Test
    @DisplayName("save debe persistir un pago")
    void saveDebeGuardarPago() {
        Pago p = new Pago();
        p.pedidoId = 1L;
        p.usuarioId = 2L;
        p.monto = new BigDecimal("999.99");
        p.metodoPago = "TARJETA";
        p.estado = "PENDIENTE";
        p.activo = true;
        Pago guardado = pagoRepository.save(p);
        assertNotNull(guardado.id);
        assertEquals("PENDIENTE", guardado.estado);
    }

    @Test
    @DisplayName("findById debe retornar pago guardado")
    void findByIdDebeRetornarPago() {
        Pago p = new Pago();
        p.pedidoId = 2L;
        p.usuarioId = 3L;
        p.monto = new BigDecimal("50.00");
        p.metodoPago = "EFECTIVO";
        p.estado = "PENDIENTE";
        p.activo = true;
        Pago guardado = pagoRepository.save(p);
        Optional<Pago> resultado = pagoRepository.findById(guardado.id);
        assertTrue(resultado.isPresent());
        assertEquals("EFECTIVO", resultado.get().metodoPago);
    }

    @Test
    @DisplayName("findAll debe retornar todos los pagos")
    void findAllDebeRetornarTodos() {
        Pago p1 = new Pago();
        p1.pedidoId = 1L; p1.usuarioId = 1L;
        p1.monto = new BigDecimal("100.00");
        p1.metodoPago = "TARJETA"; p1.estado = "PENDIENTE"; p1.activo = true;
        Pago p2 = new Pago();
        p2.pedidoId = 2L; p2.usuarioId = 2L;
        p2.monto = new BigDecimal("200.00");
        p2.metodoPago = "EFECTIVO"; p2.estado = "PENDIENTE"; p2.activo = true;
        pagoRepository.save(p1);
        pagoRepository.save(p2);
        List<Pago> lista = pagoRepository.findAll();
        assertTrue(lista.size() >= 2);
    }
}
