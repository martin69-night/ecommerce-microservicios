package cl.duoc.inventoryservice.repository;

import cl.duoc.inventoryservice.model.Inventario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("InventarioRepository - Capa Repositorio")
class InventarioRepositoryTest {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Test
    @DisplayName("save debe persistir un inventario")
    void saveDebeGuardarInventario() {
        Inventario inv = new Inventario(null, 1L, "SKU-001", 100, 0, "Bodega A", true);
        Inventario guardado = inventarioRepository.save(inv);
        assertNotNull(guardado.getId());
        assertEquals("SKU-001", guardado.getSku());
    }

    @Test
    @DisplayName("findById debe retornar inventario guardado")
    void findByIdDebeRetornarInventario() {
        Inventario inv = inventarioRepository.save(new Inventario(null, 2L, "SKU-002", 50, 0, "Bodega B", true));
        Optional<Inventario> resultado = inventarioRepository.findById(inv.getId());
        assertTrue(resultado.isPresent());
        assertEquals("SKU-002", resultado.get().getSku());
    }

    @Test
    @DisplayName("findAll debe retornar todos los inventarios")
    void findAllDebeRetornarTodos() {
        inventarioRepository.save(new Inventario(null, 3L, "SKU-003", 30, 0, "Bodega C", true));
        inventarioRepository.save(new Inventario(null, 4L, "SKU-004", 20, 0, "Bodega D", true));
        List<Inventario> lista = inventarioRepository.findAll();
        assertTrue(lista.size() >= 2);
    }
}
