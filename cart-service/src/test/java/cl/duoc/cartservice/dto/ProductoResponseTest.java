package cl.duoc.cartservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductoResponse - DTO de Catalog Service")
class ProductoResponseTest {

    @Test
    void constructorCompletoDebeAsignarTodosLosCampos() {
        ProductoResponse producto = new ProductoResponse(
                1L,
                "SKU-001",
                "Laptop",
                "Notebook para trabajo",
                new BigDecimal("999.99"),
                "Tecnologia",
                true
        );

        assertEquals(1L, producto.getId());
        assertEquals("SKU-001", producto.getSku());
        assertEquals("Laptop", producto.getNombre());
        assertEquals("Notebook para trabajo", producto.getDescripcion());
        assertEquals(new BigDecimal("999.99"), producto.getPrecio());
        assertEquals("Tecnologia", producto.getCategoria());
        assertTrue(producto.getActivo());
    }

    @Test
    void settersDebenModificarTodosLosCampos() {
        ProductoResponse producto = new ProductoResponse();

        producto.setId(2L);
        producto.setSku("SKU-002");
        producto.setNombre("Mouse");
        producto.setDescripcion("Mouse inalámbrico");
        producto.setPrecio(new BigDecimal("29.99"));
        producto.setCategoria("Accesorios");
        producto.setActivo(false);

        assertEquals(2L, producto.getId());
        assertEquals("SKU-002", producto.getSku());
        assertEquals("Mouse", producto.getNombre());
        assertEquals("Mouse inalámbrico", producto.getDescripcion());
        assertEquals(new BigDecimal("29.99"), producto.getPrecio());
        assertEquals("Accesorios", producto.getCategoria());
        assertFalse(producto.getActivo());
    }
}
