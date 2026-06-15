package cl.duoc.catalogservice.service;

import cl.duoc.catalogservice.exception.ProductoNotFoundException;
import cl.duoc.catalogservice.exception.SkuDuplicadoException;
import cl.duoc.catalogservice.model.Producto;
import cl.duoc.catalogservice.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoService - Capa Servicio")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    @DisplayName("listarActivos debe retornar lista de productos activos")
    void listarActivosDebeRetornarListaDeProductosActivos() {
        // Given
        Producto p = new Producto(1L, "SKU-001", "Laptop", "Desc", new BigDecimal("999.99"), "Tec", true);
        when(productoRepository.findByActivoTrue()).thenReturn(List.of(p));
        // When
        List<Producto> resultado = productoService.listarActivos();
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).getNombre());
        verify(productoRepository, times(1)).findByActivoTrue();
    }

    @Test
    @DisplayName("buscarPorId debe retornar producto cuando existe")
    void buscarPorIdDebeRetornarProductoCuandoExiste() {
        // Given
        Producto p = new Producto(1L, "SKU-001", "Laptop", "Desc", new BigDecimal("999.99"), "Tec", true);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        // When
        Producto resultado = productoService.buscarPorId(1L);
        // Then
        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        // Given
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        // When / Then
        assertThrows(ProductoNotFoundException.class, () -> productoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("crear debe lanzar excepción si SKU ya existe (regla de negocio)")
    void crearDebeLanzarExcepcionSiSkuYaExiste() {
        // Given
        Producto p = new Producto(null, "SKU-001", "Laptop", "Desc", new BigDecimal("999.99"), "Tec", true);
        when(productoRepository.existsBySku("SKU-001")).thenReturn(true);
        // When / Then
        assertThrows(SkuDuplicadoException.class, () -> productoService.crear(p));
        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe guardar producto cuando SKU no existe")
    void crearDebeGuardarProductoCuandoSkuNoExiste() {
        // Given
        Producto p = new Producto(null, "SKU-NEW", "Mouse", "Desc", new BigDecimal("29.99"), "Accesorios", null);
        when(productoRepository.existsBySku("SKU-NEW")).thenReturn(false);
        when(productoRepository.save(p)).thenReturn(new Producto(1L, "SKU-NEW", "Mouse", "Desc", new BigDecimal("29.99"), "Accesorios", true));
        // When
        Producto resultado = productoService.crear(p);
        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertTrue(resultado.getActivo());
    }
}
