package cl.duoc.orderservice.service;

import cl.duoc.orderservice.client.InventoryClient;
import cl.duoc.orderservice.dto.InventarioResponse;
import cl.duoc.orderservice.exception.PedidoNotFoundException;
import cl.duoc.orderservice.model.Pedido;
import cl.duoc.orderservice.repository.PedidoRepository;
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
@DisplayName("PedidoService - Capa Servicio")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedidoValido() {
        Pedido pedido = new Pedido();
        pedido.setId(99L);
        pedido.setUsuarioId(10L);
        pedido.setProductoId(100L);
        pedido.setSku("SKU-001");
        pedido.setNombreProducto("Laptop");
        pedido.setPrecioUnitario(new BigDecimal("100.00"));
        pedido.setCantidad(2);
        pedido.setEstado("PENDIENTE");
        pedido.setActivo(true);
        return pedido;
    }

    private InventarioResponse inventarioDisponible(Integer stockLibre) {
        return new InventarioResponse(
                500L,
                100L,
                "SKU-001",
                20,
                2,
                stockLibre,
                "Bodega A",
                true
        );
    }

    @Test
    @DisplayName("listarActivos debe retornar pedidos activos")
    void listarActivosDebeRetornarLista() {
        Pedido pedido = pedidoValido();
        when(pedidoRepository.findByActivoTrue()).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.listarActivos();

        assertEquals(1, resultado.size());
        assertEquals("SKU-001", resultado.get(0).getSku());
        verify(pedidoRepository).findByActivoTrue();
    }

    @Test
    @DisplayName("listarPorUsuario debe retornar pedidos activos del usuario")
    void listarPorUsuarioDebeRetornarLista() {
        Pedido pedido = pedidoValido();
        when(pedidoRepository.findByUsuarioIdAndActivoTrue(10L))
                .thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.listarPorUsuario(10L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getUsuarioId());
        verify(pedidoRepository).findByUsuarioIdAndActivoTrue(10L);
    }

    @Test
    @DisplayName("listarPorEstado debe retornar pedidos activos según estado")
    void listarPorEstadoDebeRetornarLista() {
        Pedido pedido = pedidoValido();
        pedido.setEstado("CREADO");

        when(pedidoRepository.findByEstadoAndActivoTrue("CREADO"))
                .thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.listarPorEstado("CREADO");

        assertEquals(1, resultado.size());
        assertEquals("CREADO", resultado.get(0).getEstado());
        verify(pedidoRepository).findByEstadoAndActivoTrue("CREADO");
    }

    @Test
    @DisplayName("buscarPorId debe retornar pedido cuando existe")
    void buscarPorIdDebeRetornarPedidoCuandoExiste() {
        Pedido pedido = pedidoValido();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.buscarPorId(1L);

        assertEquals(99L, resultado.getId());
        verify(pedidoRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(pedidoRepository.findById(404L)).thenReturn(Optional.empty());

        PedidoNotFoundException exception = assertThrows(
                PedidoNotFoundException.class,
                () -> pedidoService.buscarPorId(404L)
        );

        assertTrue(exception.getMessage().contains("404"));
        verify(pedidoRepository).findById(404L);
    }

    @Test
    @DisplayName("crear debe reservar stock y guardar pedido válido")
    void crearDebeReservarStockYGuardarPedido() {
        Pedido pedido = pedidoValido();
        InventarioResponse inventario = inventarioDisponible(10);

        when(inventoryClient.buscarPorSku("SKU-001")).thenReturn(inventario);
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> {
                    Pedido pedidoGuardado = invocation.getArgument(0);
                    pedidoGuardado.setId(1L);
                    return pedidoGuardado;
                });

        Pedido resultado = pedidoService.crear(pedido);

        assertEquals(1L, resultado.getId());
        assertEquals("CREADO", resultado.getEstado());
        assertTrue(resultado.getActivo());
        assertEquals(new BigDecimal("200.00"), resultado.getTotal());

        verify(inventoryClient).buscarPorSku("SKU-001");
        verify(inventoryClient).reservarStock(500L, 2);
        verify(pedidoRepository).save(pedido);
    }

    @Test
    @DisplayName("crear debe rechazar pedido sin usuarioId")
    void crearDebeRechazarPedidoSinUsuarioId() {
        Pedido pedido = pedidoValido();
        pedido.setUsuarioId(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("El usuarioId es obligatorio", exception.getMessage());
        verifyNoInteractions(inventoryClient);
    }

    @Test
    @DisplayName("crear debe rechazar pedido sin productoId")
    void crearDebeRechazarPedidoSinProductoId() {
        Pedido pedido = pedidoValido();
        pedido.setProductoId(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("El productoId es obligatorio", exception.getMessage());
        verifyNoInteractions(inventoryClient);
    }

    @Test
    @DisplayName("crear debe rechazar pedido sin SKU")
    void crearDebeRechazarPedidoSinSku() {
        Pedido pedido = pedidoValido();
        pedido.setSku(" ");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("El SKU es obligatorio", exception.getMessage());
        verifyNoInteractions(inventoryClient);
    }

    @Test
    @DisplayName("crear debe rechazar pedido sin nombre de producto")
    void crearDebeRechazarPedidoSinNombreProducto() {
        Pedido pedido = pedidoValido();
        pedido.setNombreProducto(" ");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("El nombre del producto es obligatorio", exception.getMessage());
        verifyNoInteractions(inventoryClient);
    }

    @Test
    @DisplayName("crear debe rechazar pedido con precio inválido")
    void crearDebeRechazarPedidoConPrecioInvalido() {
        Pedido pedido = pedidoValido();
        pedido.setPrecioUnitario(BigDecimal.ZERO);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("El precio unitario debe ser mayor a cero", exception.getMessage());
        verifyNoInteractions(inventoryClient);
    }

    @Test
    @DisplayName("crear debe rechazar pedido con cantidad inválida")
    void crearDebeRechazarPedidoConCantidadInvalida() {
        Pedido pedido = pedidoValido();
        pedido.setCantidad(0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("La cantidad debe ser mayor a cero", exception.getMessage());
        verifyNoInteractions(inventoryClient);
    }

    @Test
    @DisplayName("crear debe rechazar inventario inactivo")
    void crearDebeRechazarInventarioInactivo() {
        Pedido pedido = pedidoValido();
        InventarioResponse inventario = inventarioDisponible(10);
        inventario.setActivo(false);

        when(inventoryClient.buscarPorSku("SKU-001")).thenReturn(inventario);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("El inventario del producto esta inactivo", exception.getMessage());
        verify(inventoryClient, never()).reservarStock(any(), any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe rechazar pedido sin stock suficiente")
    void crearDebeRechazarPedidoConStockInsuficiente() {
        Pedido pedido = pedidoValido();
        pedido.setCantidad(10);

        when(inventoryClient.buscarPorSku("SKU-001"))
                .thenReturn(inventarioDisponible(2));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("Stock insuficiente para el SKU: SKU-001", exception.getMessage());
        verify(inventoryClient, never()).reservarStock(any(), any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe traducir 404 de inventory-service")
    void crearDebeTraducirInventarioNoEncontrado() {
        Pedido pedido = pedidoValido();
        FeignException.NotFound excepcionFeign = mock(FeignException.NotFound.class);

        when(inventoryClient.buscarPorSku("SKU-001")).thenThrow(excepcionFeign);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("No existe inventario para el SKU: SKU-001", exception.getMessage());
        verify(inventoryClient, never()).reservarStock(any(), any());
    }

    @Test
    @DisplayName("crear debe traducir error de comunicación con inventory-service")
    void crearDebeTraducirErrorComunicacionConInventoryService() {
        Pedido pedido = pedidoValido();
        FeignException excepcionFeign = mock(FeignException.class);
        when(excepcionFeign.status()).thenReturn(503);

        when(inventoryClient.buscarPorSku("SKU-001")).thenThrow(excepcionFeign);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> pedidoService.crear(pedido)
        );

        assertEquals("No fue posible comunicarse con inventory-service", exception.getMessage());
        verify(inventoryClient, never()).reservarStock(any(), any());
    }

    @Test
    @DisplayName("cambiarEstado debe actualizar estado en mayúsculas")
    void cambiarEstadoDebeActualizarEstadoEnMayusculas() {
        Pedido pedido = pedidoValido();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = pedidoService.cambiarEstado(1L, "pagado");

        assertEquals("PAGADO", resultado.getEstado());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    @DisplayName("cambiarEstado debe rechazar estado vacío")
    void cambiarEstadoDebeRechazarEstadoVacio() {
        Pedido pedido = pedidoValido();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.cambiarEstado(1L, " ")
        );

        assertEquals("El estado es obligatorio", exception.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("cancelar debe rechazar pedido pagado")
    void cancelarDebeRechazarPedidoPagado() {
        Pedido pedido = pedidoValido();
        pedido.setEstado("PAGADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.cancelar(1L)
        );

        assertEquals("No se puede cancelar un pedido pagado", exception.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("cancelar debe cambiar estado y desactivar pedido no pagado")
    void cancelarDebeCambiarEstadoYDesactivarPedido() {
        Pedido pedido = pedidoValido();
        pedido.setEstado("CREADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido resultado = pedidoService.cancelar(1L);

        assertEquals("CANCELADO", resultado.getEstado());
        assertFalse(resultado.getActivo());
        verify(pedidoRepository).save(pedido);
    }
}
