package cl.duoc.cartservice.service;

import cl.duoc.cartservice.client.CatalogClient;
import cl.duoc.cartservice.dto.ProductoResponse;
import cl.duoc.cartservice.exception.CarritoItemNotFoundException;
import cl.duoc.cartservice.exception.CarritoVacioException;
import cl.duoc.cartservice.model.CarritoItem;
import cl.duoc.cartservice.repository.CarritoItemRepository;
import feign.FeignException;
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
import static org.mockito.ArgumentMatchers.any;
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

    private CarritoItem itemValido() {
        CarritoItem item = new CarritoItem();
        item.setId(1L);
        item.setUsuarioId(10L);
        item.setProductoId(100L);
        item.setSku("SKU-001");
        item.setNombreProducto("Laptop");
        item.setPrecioUnitario(new BigDecimal("100.00"));
        item.setCantidad(2);
        item.setActivo(true);
        return item;
    }

    private ProductoResponse productoActivo() {
        return new ProductoResponse(
                100L,
                "SKU-001",
                "Laptop",
                "Notebook para trabajo",
                new BigDecimal("100.00"),
                "Tecnologia",
                true
        );
    }

    @Test
    @DisplayName("listarActivos debe retornar los items activos")
    void listarActivosDebeRetornarLista() {
        CarritoItem item = itemValido();
        when(carritoItemRepository.findByActivoTrue()).thenReturn(List.of(item));

        List<CarritoItem> resultado = carritoService.listarActivos();

        assertEquals(1, resultado.size());
        assertEquals("SKU-001", resultado.get(0).getSku());
        verify(carritoItemRepository).findByActivoTrue();
    }

    @Test
    @DisplayName("listarPorUsuario debe retornar items activos del usuario")
    void listarPorUsuarioDebeRetornarLista() {
        CarritoItem item = itemValido();
        when(carritoItemRepository.findByUsuarioIdAndActivoTrue(10L))
                .thenReturn(List.of(item));

        List<CarritoItem> resultado = carritoService.listarPorUsuario(10L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getUsuarioId());
        verify(carritoItemRepository).findByUsuarioIdAndActivoTrue(10L);
    }

    @Test
    @DisplayName("buscarPorId debe retornar item cuando existe")
    void buscarPorIdDebeRetornarItemCuandoExiste() {
        CarritoItem item = itemValido();
        when(carritoItemRepository.findById(1L)).thenReturn(Optional.of(item));

        CarritoItem resultado = carritoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        verify(carritoItemRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(carritoItemRepository.findById(99L)).thenReturn(Optional.empty());

        CarritoItemNotFoundException exception = assertThrows(
                CarritoItemNotFoundException.class,
                () -> carritoService.buscarPorId(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(carritoItemRepository).findById(99L);
    }

    @Test
    @DisplayName("agregarItem debe crear item nuevo con datos recibidos desde catalog-service")
    void agregarItemDebeCrearItemNuevoDesdeCatalogo() {
        CarritoItem item = new CarritoItem();
        item.setUsuarioId(10L);
        item.setSku("SKU-001");
        item.setCantidad(2);
        item.setActivo(null);

        ProductoResponse producto = productoActivo();

        when(catalogClient.buscarPorSku("SKU-001")).thenReturn(producto);
        when(carritoItemRepository.findByUsuarioIdAndSkuAndActivoTrue(10L, "SKU-001"))
                .thenReturn(Optional.empty());
        when(carritoItemRepository.save(any(CarritoItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CarritoItem resultado = carritoService.agregarItem(item);

        assertNull(resultado.getId());
        assertEquals(100L, resultado.getProductoId());
        assertEquals("Laptop", resultado.getNombreProducto());
        assertEquals(new BigDecimal("100.00"), resultado.getPrecioUnitario());
        assertEquals(2, resultado.getCantidad());
        assertTrue(resultado.getActivo());

        verify(catalogClient).buscarPorSku("SKU-001");
        verify(carritoItemRepository).save(item);
    }

    @Test
    @DisplayName("agregarItem debe aumentar cantidad cuando ya existe item activo con mismo SKU")
    void agregarItemDebeAcumularCantidadEnItemExistente() {
        CarritoItem existente = itemValido();
        existente.setCantidad(2);

        CarritoItem nuevo = new CarritoItem();
        nuevo.setUsuarioId(10L);
        nuevo.setSku("SKU-001");
        nuevo.setCantidad(3);

        when(catalogClient.buscarPorSku("SKU-001")).thenReturn(productoActivo());
        when(carritoItemRepository.findByUsuarioIdAndSkuAndActivoTrue(10L, "SKU-001"))
                .thenReturn(Optional.of(existente));
        when(carritoItemRepository.save(any(CarritoItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CarritoItem resultado = carritoService.agregarItem(nuevo);

        assertEquals(5, resultado.getCantidad());
        assertEquals(100L, resultado.getProductoId());
        assertEquals("Laptop", resultado.getNombreProducto());
        assertEquals(new BigDecimal("100.00"), resultado.getPrecioUnitario());

        verify(carritoItemRepository).save(existente);
    }

    @Test
    @DisplayName("agregarItem debe rechazar solicitud sin usuarioId")
    void agregarItemDebeRechazarSinUsuarioId() {
        CarritoItem item = itemValido();
        item.setUsuarioId(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> carritoService.agregarItem(item)
        );

        assertEquals("El usuarioId es obligatorio", exception.getMessage());
        verifyNoInteractions(catalogClient);
    }

    @Test
    @DisplayName("agregarItem debe rechazar solicitud sin SKU")
    void agregarItemDebeRechazarSinSku() {
        CarritoItem item = itemValido();
        item.setSku(" ");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> carritoService.agregarItem(item)
        );

        assertEquals("El SKU es obligatorio", exception.getMessage());
        verifyNoInteractions(catalogClient);
    }

    @Test
    @DisplayName("agregarItem debe rechazar cantidad inválida")
    void agregarItemDebeRechazarCantidadInvalida() {
        CarritoItem item = itemValido();
        item.setCantidad(0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> carritoService.agregarItem(item)
        );

        assertEquals("La cantidad debe ser mayor a cero", exception.getMessage());
        verifyNoInteractions(catalogClient);
    }

    @Test
    @DisplayName("agregarItem debe rechazar producto nulo desde catalog-service")
    void agregarItemDebeRechazarProductoNuloDesdeCatalogo() {
        CarritoItem item = itemValido();
        when(catalogClient.buscarPorSku("SKU-001")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> carritoService.agregarItem(item)
        );

        assertEquals("El producto no existe en catalog-service", exception.getMessage());
        verify(carritoItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("agregarItem debe rechazar producto inactivo desde catalog-service")
    void agregarItemDebeRechazarProductoInactivoDesdeCatalogo() {
        CarritoItem item = itemValido();
        ProductoResponse producto = productoActivo();
        producto.setActivo(false);

        when(catalogClient.buscarPorSku("SKU-001")).thenReturn(producto);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> carritoService.agregarItem(item)
        );

        assertEquals(
                "El producto existe pero esta inactivo en catalog-service",
                exception.getMessage()
        );
        verify(carritoItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("agregarItem debe rechazar producto sin precio válido")
    void agregarItemDebeRechazarProductoConPrecioInvalido() {
        CarritoItem item = itemValido();
        ProductoResponse producto = productoActivo();
        producto.setPrecio(BigDecimal.ZERO);

        when(catalogClient.buscarPorSku("SKU-001")).thenReturn(producto);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> carritoService.agregarItem(item)
        );

        assertEquals(
                "El producto no tiene precio valido en catalog-service",
                exception.getMessage()
        );
        verify(carritoItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("agregarItem debe traducir respuesta 404 del catalog-service")
    void agregarItemDebeTraducirProductoNoEncontradoDesdeCatalogo() {
        CarritoItem item = itemValido();
        FeignException.NotFound excepcionFeign = mock(FeignException.NotFound.class);

        when(catalogClient.buscarPorSku("SKU-001")).thenThrow(excepcionFeign);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> carritoService.agregarItem(item)
        );

        assertEquals("No existe un producto con SKU: SKU-001", exception.getMessage());
        verify(carritoItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("agregarItem debe traducir error de comunicación con catalog-service")
    void agregarItemDebeTraducirErrorComunicacionConCatalogo() {
        CarritoItem item = itemValido();
        FeignException excepcionFeign = mock(FeignException.class);
        when(excepcionFeign.status()).thenReturn(503);

        when(catalogClient.buscarPorSku("SKU-001")).thenThrow(excepcionFeign);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> carritoService.agregarItem(item)
        );

        assertEquals("No fue posible comunicarse con catalog-service", exception.getMessage());
        verify(carritoItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarCantidad debe cambiar cantidad de item existente")
    void actualizarCantidadDebeCambiarCantidad() {
        CarritoItem item = itemValido();
        when(carritoItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(carritoItemRepository.save(any(CarritoItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CarritoItem resultado = carritoService.actualizarCantidad(1L, 5);

        assertEquals(5, resultado.getCantidad());
        verify(carritoItemRepository).save(item);
    }

    @Test
    @DisplayName("actualizarCantidad debe rechazar cantidad inválida")
    void actualizarCantidadDebeRechazarCantidadInvalida() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> carritoService.actualizarCantidad(1L, 0)
        );

        assertEquals("La cantidad debe ser mayor a cero", exception.getMessage());
        verify(carritoItemRepository, never()).findById(any());
    }

    @Test
    @DisplayName("eliminarItem debe desactivar item")
    void eliminarItemDebeDesactivarItem() {
        CarritoItem item = itemValido();
        when(carritoItemRepository.findById(1L)).thenReturn(Optional.of(item));

        carritoService.eliminarItem(1L);

        assertFalse(item.getActivo());
        verify(carritoItemRepository).save(item);
    }

    @Test
    @DisplayName("vaciarCarrito debe desactivar todos los items activos del usuario")
    void vaciarCarritoDebeDesactivarItems() {
        CarritoItem itemUno = itemValido();
        CarritoItem itemDos = itemValido();
        itemDos.setId(2L);

        List<CarritoItem> items = List.of(itemUno, itemDos);
        when(carritoItemRepository.findByUsuarioIdAndActivoTrue(10L)).thenReturn(items);

        carritoService.vaciarCarrito(10L);

        assertFalse(itemUno.getActivo());
        assertFalse(itemDos.getActivo());
        verify(carritoItemRepository).saveAll(items);
    }

    @Test
    @DisplayName("vaciarCarrito debe lanzar excepción si no existen items activos")
    void vaciarCarritoDebeLanzarExcepcionSiCarritoVacio() {
        when(carritoItemRepository.findByUsuarioIdAndActivoTrue(10L)).thenReturn(List.of());

        CarritoVacioException exception = assertThrows(
                CarritoVacioException.class,
                () -> carritoService.vaciarCarrito(10L)
        );

        assertEquals("El usuario no tiene items activos en el carrito", exception.getMessage());
        verify(carritoItemRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("calcularTotal debe retornar suma de subtotales")
    void calcularTotalDebeRetornarSumaDeSubtotales() {
        CarritoItem itemUno = itemValido();
        itemUno.setPrecioUnitario(new BigDecimal("100.00"));
        itemUno.setCantidad(2);

        CarritoItem itemDos = itemValido();
        itemDos.setPrecioUnitario(new BigDecimal("50.00"));
        itemDos.setCantidad(1);

        when(carritoItemRepository.findByUsuarioIdAndActivoTrue(10L))
                .thenReturn(List.of(itemUno, itemDos));

        BigDecimal total = carritoService.calcularTotal(10L);

        assertEquals(new BigDecimal("250.00"), total);
    }

    @Test
    @DisplayName("calcularTotal debe retornar cero cuando el carrito está vacío")
    void calcularTotalDebeRetornarCeroCuandoCarritoVacio() {
        when(carritoItemRepository.findByUsuarioIdAndActivoTrue(10L)).thenReturn(List.of());

        BigDecimal total = carritoService.calcularTotal(10L);

        assertEquals(BigDecimal.ZERO, total);
    }
}
