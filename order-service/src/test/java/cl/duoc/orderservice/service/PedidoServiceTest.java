package cl.duoc.orderservice.service;

import cl.duoc.orderservice.client.InventoryClient;
import cl.duoc.orderservice.dto.InventarioResponse;
import cl.duoc.orderservice.exception.PedidoNotFoundException;
import cl.duoc.orderservice.model.Pedido;
import cl.duoc.orderservice.repository.PedidoRepository;
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
@DisplayName("PedidoService - Capa Servicio")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("listarActivos debe retornar lista de pedidos activos")
    void listarActivosDebeRetornarLista() {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setEstado("CREADO");
        when(pedidoRepository.findByActivoTrue()).thenReturn(List.of(p));
        List<Pedido> resultado = pedidoService.listarActivos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findByActivoTrue();
    }

    @Test
    @DisplayName("buscarPorId debe retornar pedido cuando existe")
    void buscarPorIdDebeRetornarPedidoCuandoExiste() {
        Pedido p = new Pedido();
        p.setId(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(p));
        Pedido resultado = pedidoService.buscarPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(PedidoNotFoundException.class, () -> pedidoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("crear debe lanzar excepción si stock insuficiente (regla de negocio)")
    void crearDebeLanzarExcepcionSiStockInsuficiente() {
        Pedido p = new Pedido();
        p.setUsuarioId(1L);
        p.setProductoId(2L);
        p.setSku("SKU-001");
        p.setNombreProducto("Laptop");
        p.setPrecioUnitario(new BigDecimal("999.99"));
        p.setCantidad(10);

        InventarioResponse inv = mock(InventarioResponse.class);
        when(inv.getActivo()).thenReturn(true);
        when(inv.getStockLibre()).thenReturn(2);

        when(inventoryClient.buscarPorSku("SKU-001")).thenReturn(inv);
        assertThrows(IllegalArgumentException.class, () -> pedidoService.crear(p));
    }

    @Test
    @DisplayName("cancelar debe lanzar excepción si pedido ya está pagado (regla de negocio)")
    void cancelarDebeLanzarExcepcionSiPedidoYaEstaPagado() {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setEstado("PAGADO");
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(p));
        assertThrows(IllegalArgumentException.class, () -> pedidoService.cancelar(1L));
    }
}
