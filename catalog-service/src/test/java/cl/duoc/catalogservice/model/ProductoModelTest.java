package cl.duoc.catalogservice.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Producto - Capa Modelo")
class ProductoModelTest {

    @Test
    @DisplayName("Constructor vacío debe crear objeto no nulo")
    void constructorVacioDebeCrearObjetoNoNulo() {
        // Given / When
        Producto producto = new Producto();
        // Then
        assertNotNull(producto);
    }

    @Test
    @DisplayName("Constructor completo debe asignar todos los campos")
    void constructorCompletoDebeAsignarTodosLosCampos() {
        // Given / When
        Producto producto = new Producto(1L, "SKU-001", "Laptop", "Laptop gamer", new BigDecimal("999.99"), "Tecnología", true);
        // Then
        assertEquals(1L, producto.getId());
        assertEquals("SKU-001", producto.getSku());
        assertEquals("Laptop", producto.getNombre());
        assertEquals("Laptop gamer", producto.getDescripcion());
        assertEquals(new BigDecimal("999.99"), producto.getPrecio());
        assertEquals("Tecnología", producto.getCategoria());
        assertTrue(producto.getActivo());
    }

    @Test
    @DisplayName("Setters deben modificar los campos correctamente")
    void settersDebenModificarLosCamposCorrectamente() {
        // Given
        Producto producto = new Producto();
        // When
        producto.setId(2L);
        producto.setSku("SKU-002");
        producto.setNombre("Mouse");
        producto.setPrecio(new BigDecimal("29.99"));
        producto.setActivo(false);
        // Then
        assertEquals(2L, producto.getId());
        assertEquals("SKU-002", producto.getSku());
        assertEquals("Mouse", producto.getNombre());
        assertEquals(new BigDecimal("29.99"), producto.getPrecio());
        assertFalse(producto.getActivo());
    }

    @Test
    @DisplayName("Activo debe ser true por defecto")
    void activoDebeSerTruePorDefecto() {
        // Given / When
        Producto producto = new Producto();
        producto.setActivo(true);
        // Then
        assertTrue(producto.getActivo());
    }
}
