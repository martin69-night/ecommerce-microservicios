package cl.duoc.orderservice.controller;

import cl.duoc.orderservice.model.Pedido;
import cl.duoc.orderservice.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoController - Capa Controlador")
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private Pedido pedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuarioId(10L);
        pedido.setProductoId(100L);
        pedido.setSku("SKU-001");
        pedido.setNombreProducto("Laptop");
        pedido.setPrecioUnitario(new BigDecimal("100.00"));
        pedido.setCantidad(2);
        pedido.setTotal(new BigDecimal("200.00"));
        pedido.setEstado("CREADO");
        pedido.setActivo(true);
        return pedido;
    }

    @Test
    void listarActivosDebeRetornarOk() {
        when(pedidoService.listarActivos()).thenReturn(List.of(pedido()));

        ResponseEntity<List<Pedido>> respuesta = pedidoController.listarActivos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(pedidoService).listarActivos();
    }

    @Test
    void listarPorUsuarioDebeRetornarOk() {
        when(pedidoService.listarPorUsuario(10L)).thenReturn(List.of(pedido()));

        ResponseEntity<List<Pedido>> respuesta = pedidoController.listarPorUsuario(10L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(pedidoService).listarPorUsuario(10L);
    }

    @Test
    void listarPorEstadoDebeConvertirEstadoAMayusculas() {
        when(pedidoService.listarPorEstado("CREADO")).thenReturn(List.of(pedido()));

        ResponseEntity<List<Pedido>> respuesta = pedidoController.listarPorEstado("creado");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(pedidoService).listarPorEstado("CREADO");
    }

    @Test
    void buscarPorIdDebeRetornarOk() {
        when(pedidoService.buscarPorId(1L)).thenReturn(pedido());

        ResponseEntity<Pedido> respuesta = pedidoController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1L, respuesta.getBody().getId());
        verify(pedidoService).buscarPorId(1L);
    }

    @Test
    void crearDebeRetornarCreated() {
        Pedido pedido = pedido();
        when(pedidoService.crear(pedido)).thenReturn(pedido);

        ResponseEntity<Pedido> respuesta = pedidoController.crear(pedido);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(pedido, respuesta.getBody());
        verify(pedidoService).crear(pedido);
    }

    @Test
    void cambiarEstadoDebeRetornarOk() {
        Pedido pedido = pedido();
        pedido.setEstado("PAGADO");
        when(pedidoService.cambiarEstado(1L, "PAGADO")).thenReturn(pedido);

        ResponseEntity<Pedido> respuesta = pedidoController.cambiarEstado(1L, "PAGADO");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("PAGADO", respuesta.getBody().getEstado());
        verify(pedidoService).cambiarEstado(1L, "PAGADO");
    }

    @Test
    void cancelarDebeRetornarOk() {
        Pedido pedido = pedido();
        pedido.setEstado("CANCELADO");
        pedido.setActivo(false);

        when(pedidoService.cancelar(1L)).thenReturn(pedido);

        ResponseEntity<Pedido> respuesta = pedidoController.cancelar(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("CANCELADO", respuesta.getBody().getEstado());
        assertFalse(respuesta.getBody().getActivo());
        verify(pedidoService).cancelar(1L);
    }
}
