package cl.duoc.inventoryservice.service;

import cl.duoc.inventoryservice.exception.InventarioNotFoundException;
import cl.duoc.inventoryservice.exception.SkuInventarioDuplicadoException;
import cl.duoc.inventoryservice.exception.StockInsuficienteException;
import cl.duoc.inventoryservice.model.Inventario;
import cl.duoc.inventoryservice.repository.InventarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioService - Capa Servicio")
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    @DisplayName("listarActivos debe retornar lista de inventarios activos")
    void listarActivosDebeRetornarLista() {
        // Given
        Inventario inv = new Inventario(1L, 10L, "SKU-001", 100, 20, "Bodega A", true);
        when(inventarioRepository.findByActivoTrue()).thenReturn(List.of(inv));
        // When
        List<Inventario> resultado = inventarioService.listarActivos();
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inventarioRepository, times(1)).findByActivoTrue();
    }

    @Test
    @DisplayName("buscarPorId debe retornar inventario cuando existe")
    void buscarPorIdDebeRetornarInventarioCuandoExiste() {
        // Given
        Inventario inv = new Inventario(1L, 10L, "SKU-001", 100, 20, "Bodega A", true);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inv));
        // When
        Inventario resultado = inventarioService.buscarPorId(1L);
        // Then
        assertEquals("SKU-001", resultado.getSku());
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        // Given
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());
        // When / Then
        assertThrows(InventarioNotFoundException.class, () -> inventarioService.buscarPorId(99L));
    }

    @Test
    @DisplayName("crear debe lanzar excepción si SKU ya existe (regla de negocio)")
    void crearDebeLanzarExcepcionSiSkuYaExiste() {
        // Given
        Inventario inv = new Inventario(null, 10L, "SKU-001", 100, 0, "Bodega A", true);
        when(inventarioRepository.existsBySku("SKU-001")).thenReturn(true);
        // When / Then
        assertThrows(SkuInventarioDuplicadoException.class, () -> inventarioService.crear(inv));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("reservarStock debe lanzar excepción si stock libre es insuficiente (regla de negocio)")
    void reservarStockDebeLanzarExcepcionSiStockInsuficiente() {
        // Given
        Inventario inv = new Inventario(1L, 10L, "SKU-001", 10, 8, "Bodega A", true);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inv));
        // When / Then — stock libre = 2, intentamos reservar 5
        assertThrows(StockInsuficienteException.class, () -> inventarioService.reservarStock(1L, 5));
    }

    @Test
    @DisplayName("validarCantidad debe lanzar excepción si cantidad es cero")
    void reservarStockDebeLanzarExcepcionSiCantidadEsCero() {
        assertThrows(IllegalArgumentException.class, () -> inventarioService.reservarStock(1L, 0));
    }
}
