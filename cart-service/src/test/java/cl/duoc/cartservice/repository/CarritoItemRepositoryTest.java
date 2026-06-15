package cl.duoc.cartservice.repository;

import cl.duoc.cartservice.model.CarritoItem;
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
@DisplayName("CarritoItemRepository - Capa Repositorio")
class CarritoItemRepositoryTest {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Test
    @DisplayName("save debe persistir un item")
    void saveDebeGuardarItem() {
        CarritoItem item = new CarritoItem(null, 1L, 2L, "SKU-001", "Laptop", new BigDecimal("999.99"), 1, true, null);
        CarritoItem guardado = carritoItemRepository.save(item);
        assertNotNull(guardado.getId());
        assertEquals("SKU-001", guardado.getSku());
    }

    @Test
    @DisplayName("findById debe retornar item guardado")
    void findByIdDebeRetornarItem() {
        CarritoItem item = carritoItemRepository.save(
            new CarritoItem(null, 1L, 3L, "SKU-002", "Mouse", new BigDecimal("29.99"), 2, true, null));
        Optional<CarritoItem> resultado = carritoItemRepository.findById(item.getId());
        assertTrue(resultado.isPresent());
        assertEquals("SKU-002", resultado.get().getSku());
    }

    @Test
    @DisplayName("findAll debe retornar todos los items")
    void findAllDebeRetornarTodos() {
        carritoItemRepository.save(new CarritoItem(null, 1L, 1L, "SKU-003", "Teclado", new BigDecimal("49.99"), 1, true, null));
        carritoItemRepository.save(new CarritoItem(null, 2L, 2L, "SKU-004", "Monitor", new BigDecimal("299.99"), 1, true, null));
        List<CarritoItem> lista = carritoItemRepository.findAll();
        assertTrue(lista.size() >= 2);
    }
}
