package cl.duoc.paymentservice.controller;

import cl.duoc.paymentservice.model.Pago;
import cl.duoc.paymentservice.service.PagoService;
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
@DisplayName("PagoController - Capa Controlador")
class PagoControllerTest {

    @Mock
    private PagoService pagoService;

    @InjectMocks
    private PagoController pagoController;

    private Pago pago() {
        Pago pago = new Pago();
        pago.id = 1L;
        pago.pedidoId = 10L;
        pago.usuarioId = 20L;
        pago.monto = new BigDecimal("15000.00");
        pago.metodoPago = "TARJETA";
        pago.estado = "PENDIENTE";
        pago.activo = true;
        return pago;
    }

    @Test
    void listarActivosDebeRetornarOk() {
        when(pagoService.listarActivos()).thenReturn(List.of(pago()));

        ResponseEntity<List<Pago>> respuesta = pagoController.listarActivos();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(pagoService).listarActivos();
    }

    @Test
    void listarPorUsuarioDebeRetornarOk() {
        when(pagoService.listarPorUsuario(20L)).thenReturn(List.of(pago()));

        ResponseEntity<List<Pago>> respuesta = pagoController.listarPorUsuario(20L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(pagoService).listarPorUsuario(20L);
    }

    @Test
    void listarPorEstadoDebeRetornarOk() {
        when(pagoService.listarPorEstado("PENDIENTE")).thenReturn(List.of(pago()));

        ResponseEntity<List<Pago>> respuesta = pagoController.listarPorEstado("PENDIENTE");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(pagoService).listarPorEstado("PENDIENTE");
    }

    @Test
    void buscarPorIdDebeRetornarOk() {
        when(pagoService.buscarPorId(1L)).thenReturn(pago());

        ResponseEntity<Pago> respuesta = pagoController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1L, respuesta.getBody().id);
        verify(pagoService).buscarPorId(1L);
    }

    @Test
    void buscarPorPedidoIdDebeRetornarOk() {
        when(pagoService.buscarPorPedidoId(10L)).thenReturn(pago());

        ResponseEntity<Pago> respuesta = pagoController.buscarPorPedidoId(10L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(10L, respuesta.getBody().pedidoId);
        verify(pagoService).buscarPorPedidoId(10L);
    }

    @Test
    void crearDebeRetornarCreated() {
        Pago pago = pago();
        when(pagoService.crear(pago)).thenReturn(pago);

        ResponseEntity<Pago> respuesta = pagoController.crear(pago);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertSame(pago, respuesta.getBody());
        verify(pagoService).crear(pago);
    }

    @Test
    void aprobarDebeRetornarOk() {
        Pago pago = pago();
        pago.estado = "APROBADO";
        when(pagoService.aprobar(1L)).thenReturn(pago);

        ResponseEntity<Pago> respuesta = pagoController.aprobar(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("APROBADO", respuesta.getBody().estado);
        verify(pagoService).aprobar(1L);
    }

    @Test
    void rechazarDebeRetornarOk() {
        Pago pago = pago();
        pago.estado = "RECHAZADO";
        when(pagoService.rechazar(1L)).thenReturn(pago);

        ResponseEntity<Pago> respuesta = pagoController.rechazar(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("RECHAZADO", respuesta.getBody().estado);
        verify(pagoService).rechazar(1L);
    }

    @Test
    void anularDebeRetornarOk() {
        Pago pago = pago();
        pago.estado = "ANULADO";
        pago.activo = false;
        when(pagoService.anular(1L)).thenReturn(pago);

        ResponseEntity<Pago> respuesta = pagoController.anular(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("ANULADO", respuesta.getBody().estado);
        assertFalse(respuesta.getBody().activo);
        verify(pagoService).anular(1L);
    }
}
