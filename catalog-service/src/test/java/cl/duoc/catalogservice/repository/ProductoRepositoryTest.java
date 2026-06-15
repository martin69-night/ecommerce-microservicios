package cl.duoc.catalogservice.repository;

import cl.duoc.catalogservice.model.Producto;
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
@DisplayName("ProductoRepository - Capa Repositorio")
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    @DisplayName("save debe persistir un producto")
    void saveDebeGuardarProducto() {
        Producto p = new Producto(null, "SKU-001", "Laptop", "Desc", new BigDecimal("999.99"), "Tec", true);
        Producto guardado = productoRepository.save(p);
        assertNotNull(guardado.getId());
        assertEquals("SKU-001", guardado.getSku());
    }

    @Test
    @DisplayName("findById debe retornar producto guardado")
    void findByIdDebeRetornarProducto() {
        Producto p = productoRepository.save(new Producto(null, "SKU-002", "Mouse", "Desc", new BigDecimal("29.99"), "Acc", true));
        Optional<Producto> resultado = productoRepository.findById(p.getId());
        assertTrue(resultado.isPresent());
        assertEquals("Mouse", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findAll debe retornar todos los productos")
    void findAllDebeRetornarTodos() {
        productoRepository.save(new Producto(null, "SKU-003", "Teclado", "Desc", new BigDecimal("49.99"), "Acc", true));
        productoRepository.save(new Producto(null, "SKU-004", "Monitor", "Desc", new BigDecimal("299.99"), "Tec", true));
        List<Producto> lista = productoRepository.findAll();
        assertTrue(lista.size() >= 2);
    }
}
