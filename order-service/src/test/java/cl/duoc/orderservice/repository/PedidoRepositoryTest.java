package cl.duoc.orderservice.repository;

import cl.duoc.orderservice.model.Pedido;
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
@DisplayName("PedidoRepository - Capa Repositorio")
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    @DisplayName("save debe persistir un pedido")
    void saveDebeGuardarPedido() {
        Pedido p = new Pedido();
        p.setUsuarioId(1L);
        p.setProductoId(2L);
        p.setSku("SKU-001");
        p.setNombreProducto("Laptop");
        p.setPrecioUnitario(new BigDecimal("999.99"));
        p.setCantidad(1);
        p.setEstado("CREADO");
        p.setActivo(true);
        p.calcularTotal();
        Pedido guardado = pedidoRepository.save(p);
        assertNotNull(guardado.getId());
        assertEquals("SKU-001", guardado.getSku());
    }

    @Test
    @DisplayName("findById debe retornar pedido guardado")
    void findByIdDebeRetornarPedido() {
        Pedido p = new Pedido();
        p.setUsuarioId(1L);
        p.setProductoId(2L);
        p.setSku("SKU-002");
        p.setNombreProducto("Mouse");
        p.setPrecioUnitario(new BigDecimal("29.99"));
        p.setCantidad(2);
        p.setEstado("CREADO");
        p.setActivo(true);
        p.calcularTotal();
        Pedido guardado = pedidoRepository.save(p);
        Optional<Pedido> resultado = pedidoRepository.findById(guardado.getId());
        assertTrue(resultado.isPresent());
        assertEquals("SKU-002", resultado.get().getSku());
    }

    @Test
    @DisplayName("findAll debe retornar todos los pedidos")
    void findAllDebeRetornarTodos() {
        Pedido p1 = new Pedido();
        p1.setUsuarioId(1L); p1.setProductoId(1L); p1.setSku("SKU-A");
        p1.setNombreProducto("A"); p1.setPrecioUnitario(new BigDecimal("10.00"));
        p1.setCantidad(1); p1.setEstado("CREADO"); p1.setActivo(true); p1.calcularTotal();
        Pedido p2 = new Pedido();
        p2.setUsuarioId(2L); p2.setProductoId(2L); p2.setSku("SKU-B");
        p2.setNombreProducto("B"); p2.setPrecioUnitario(new BigDecimal("20.00"));
        p2.setCantidad(1); p2.setEstado("CREADO"); p2.setActivo(true); p2.calcularTotal();
        pedidoRepository.save(p1);
        pedidoRepository.save(p2);
        List<Pedido> lista = pedidoRepository.findAll();
        assertTrue(lista.size() >= 2);
    }
}
