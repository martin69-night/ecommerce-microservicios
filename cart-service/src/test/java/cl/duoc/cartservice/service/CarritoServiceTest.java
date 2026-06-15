package cl.duoc.cartservice.service;

import cl.duoc.cartservice.client.CatalogClient;
import cl.duoc.cartservice.dto.ProductoResponse;
import cl.duoc.cartservice.exception.CarritoItemNotFoundException;
import cl.duoc.cartservice.exception.CarritoVacioException;
import cl.duoc.cartservice.model.CarritoItem;
import cl.duoc.cartservice.repository.CarritoItemRepository;
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
@DisplayName("CarritoService - Capa Servicio")
class CarritoServiceTest {

    @Mock
    private CarritoItemRepository carritoItemRepository;

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private CarritoService carritoService;

    @Test
    @DisplayName("listarActivos debe retornar lista de items activos")
    void listarActivosDebeRetornarLista() {
        // Given
        CarritoItem item = new CarritoItem();
        item.setId(1L);
        when(carritoItemRepository.findByActivoTrue()).thenReturn(List.of(item));
        // When
        List<CarritoItem> resultado = carritoService.listarActivos();
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(carritoItemRepository, times(1)).findByActivoTrue();
    }

    @Test
    @DisplayName("buscarPorId debe retornar item cuando existe")
    void buscarPorIdDebeRetornarItemCuandoExiste() {
        // Given
        CarritoItem item = new CarritoItem();
        item.setId(1L);
        when(carritoItemRepository.findById(1L)).thenReturn(Optional.of(item));
        // When
        CarritoItem resultado = carritoService.buscarPorId(1L);
        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        // Given
        when(carritoItemRepository.findById(99L)).thenReturn(Optional.empty());
        // When / Then
        assertThrows(CarritoItemNotFoundException.class, () -> carritoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("vaciarCarrito debe lanzar excepción si carrito está vacío (regla de negocio)")
    void vaciarCarritoDebeLanzarExcepcionSiCarritoVacio() {
        // Given
        when(carritoItemRepository.findByUsuarioIdAndActivoTrue(1L)).thenReturn(List.of());
        // When / Then
        assertThrows(CarritoVacioException.class, () -> carritoService.vaciarCarrito(1L));
    }

    @Test
    @DisplayName("agregarItem debe lanzar excepción si cantidad es cero (regla de negocio)")
    void agregarItemDebeLanzarExcepcionSiCantidadEsCero() {
        // Given
        CarritoItem item = new CarritoItem();
        item.setUsuarioId(1L);
        item.setSku("SKU-001");
        item.setCantidad(0);
        // When / Then
        assertThrows(IllegalArgumentException.class, () -> carritoService.agregarItem(item));
    }

    @Test
    @DisplayName("calcularTotal debe retornar suma de subtotales")
    void calcularTotalDebeRetornarSumaDeSubtotales() {
        // Given
        CarritoItem item1 = new CarritoItem();
        item1.setPrecioUnitario(new BigDecimal("100.00"));
        item1.setCantidad(2);
        CarritoItem item2 = new CarritoItem();
        item2.setPrecioUnitario(new BigDecimal("50.00"));
        item2.setCantidad(1);
        when(carritoItemRepository.findByUsuarioIdAndActivoTrue(1L)).thenReturn(List.of(item1, item2));
        // When
        BigDecimal total = carritoService.calcularTotal(1L);
        // Then
        assertEquals(new BigDecimal("250.00"), total);
    }
}
