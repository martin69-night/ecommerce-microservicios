package cl.duoc.paymentservice.service;

import cl.duoc.paymentservice.exception.PagoInvalidoException;
import cl.duoc.paymentservice.exception.PagoNotFoundException;
import cl.duoc.paymentservice.model.Pago;
import cl.duoc.paymentservice.repository.PagoRepository;
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
@DisplayName("PagoService - Capa Servicio")
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pagoValido() {
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
    @DisplayName("listarActivos debe retornar lista de pagos activos")
    void listarActivosDebeRetornarLista() {
        Pago pago = pagoValido();
        when(pagoRepository.findByActivoTrue()).thenReturn(List.of(pago));

        List<Pago> resultado = pagoService.listarActivos();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).id);
        verify(pagoRepository).findByActivoTrue();
    }

    @Test
    @DisplayName("listarPorUsuario debe retornar pagos activos del usuario")
    void listarPorUsuarioDebeRetornarLista() {
        Pago pago = pagoValido();
        when(pagoRepository.findByUsuarioIdAndActivoTrue(20L)).thenReturn(List.of(pago));

        List<Pago> resultado = pagoService.listarPorUsuario(20L);

        assertEquals(1, resultado.size());
        assertEquals(20L, resultado.get(0).usuarioId);
        verify(pagoRepository).findByUsuarioIdAndActivoTrue(20L);
    }

    @Test
    @DisplayName("listarPorEstado debe convertir estado a mayúsculas")
    void listarPorEstadoDebeConvertirAMayusculas() {
        Pago pago = pagoValido();
        when(pagoRepository.findByEstadoAndActivoTrue("PENDIENTE")).thenReturn(List.of(pago));

        List<Pago> resultado = pagoService.listarPorEstado("pendiente");

        assertEquals(1, resultado.size());
        verify(pagoRepository).findByEstadoAndActivoTrue("PENDIENTE");
    }

    @Test
    @DisplayName("buscarPorId debe retornar pago cuando existe")
    void buscarPorIdDebeRetornarPagoCuandoExiste() {
        Pago pago = pagoValido();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        Pago resultado = pagoService.buscarPorId(1L);

        assertEquals(1L, resultado.id);
        verify(pagoRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        PagoNotFoundException exception = assertThrows(
                PagoNotFoundException.class,
                () -> pagoService.buscarPorId(99L)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(pagoRepository).findById(99L);
    }

    @Test
    @DisplayName("buscarPorPedidoId debe retornar pago cuando existe")
    void buscarPorPedidoIdDebeRetornarPagoCuandoExiste() {
        Pago pago = pagoValido();
        when(pagoRepository.findByPedidoIdAndActivoTrue(10L)).thenReturn(Optional.of(pago));

        Pago resultado = pagoService.buscarPorPedidoId(10L);

        assertEquals(10L, resultado.pedidoId);
        verify(pagoRepository).findByPedidoIdAndActivoTrue(10L);
    }

    @Test
    @DisplayName("buscarPorPedidoId debe lanzar excepción cuando no existe")
    void buscarPorPedidoIdDebeLanzarExcepcionCuandoNoExiste() {
        when(pagoRepository.findByPedidoIdAndActivoTrue(999L)).thenReturn(Optional.empty());

        PagoNotFoundException exception = assertThrows(
                PagoNotFoundException.class,
                () -> pagoService.buscarPorPedidoId(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(pagoRepository).findByPedidoIdAndActivoTrue(999L);
    }

    @Test
    @DisplayName("crear debe inicializar y guardar un pago pendiente activo")
    void crearDebeInicializarYGuardarPago() {
        Pago pago = pagoValido();
        pago.id = 999L;
        pago.estado = "APROBADO";
        pago.activo = false;

        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pago resultado = pagoService.crear(pago);

        assertNull(resultado.id);
        assertEquals("PENDIENTE", resultado.estado);
        assertTrue(resultado.activo);
        verify(pagoRepository).save(pago);
    }

    @Test
    @DisplayName("crear debe rechazar pago sin pedidoId")
    void crearDebeRechazarPagoSinPedidoId() {
        Pago pago = pagoValido();
        pago.pedidoId = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals("El pedidoId es obligatorio", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe rechazar pago sin usuarioId")
    void crearDebeRechazarPagoSinUsuarioId() {
        Pago pago = pagoValido();
        pago.usuarioId = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals("El usuarioId es obligatorio", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe rechazar pago con monto nulo")
    void crearDebeRechazarPagoConMontoNulo() {
        Pago pago = pagoValido();
        pago.monto = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe rechazar pago con monto cero")
    void crearDebeRechazarPagoConMontoCero() {
        Pago pago = pagoValido();
        pago.monto = BigDecimal.ZERO;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe rechazar pago sin método de pago")
    void crearDebeRechazarPagoSinMetodoPago() {
        Pago pago = pagoValido();
        pago.metodoPago = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals("El metodo de pago es obligatorio", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear debe rechazar pago con método de pago vacío")
    void crearDebeRechazarPagoConMetodoPagoVacio() {
        Pago pago = pagoValido();
        pago.metodoPago = "   ";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals("El metodo de pago es obligatorio", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("aprobar debe aprobar pago pendiente y generar código de transacción")
    void aprobarDebeAprobarPagoPendiente() {
        Pago pago = pagoValido();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pago resultado = pagoService.aprobar(1L);

        assertEquals("APROBADO", resultado.estado);
        assertNotNull(resultado.codigoTransaccion);
        assertTrue(resultado.codigoTransaccion.startsWith("TX-"));
        verify(pagoRepository).save(pago);
    }

    @Test
    @DisplayName("aprobar debe lanzar excepción si pago no está pendiente")
    void aprobarDebeLanzarExcepcionSiNoEstaPendiente() {
        Pago pago = pagoValido();
        pago.estado = "APROBADO";
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        PagoInvalidoException exception = assertThrows(
                PagoInvalidoException.class,
                () -> pagoService.aprobar(1L)
        );

        assertEquals("Solo se pueden aprobar pagos pendientes", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("rechazar debe cambiar estado de pago pendiente a rechazado")
    void rechazarDebeCambiarEstadoARechazado() {
        Pago pago = pagoValido();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pago resultado = pagoService.rechazar(1L);

        assertEquals("RECHAZADO", resultado.estado);
        verify(pagoRepository).save(pago);
    }

    @Test
    @DisplayName("rechazar debe lanzar excepción si pago no está pendiente")
    void rechazarDebeLanzarExcepcionSiNoEstaPendiente() {
        Pago pago = pagoValido();
        pago.estado = "RECHAZADO";
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        PagoInvalidoException exception = assertThrows(
                PagoInvalidoException.class,
                () -> pagoService.rechazar(1L)
        );

        assertEquals("Solo se pueden rechazar pagos pendientes", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("anular debe desactivar un pago no aprobado")
    void anularDebeDesactivarPagoNoAprobado() {
        Pago pago = pagoValido();
        pago.estado = "RECHAZADO";
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pago resultado = pagoService.anular(1L);

        assertEquals("ANULADO", resultado.estado);
        assertFalse(resultado.activo);
        verify(pagoRepository).save(pago);
    }

    @Test
    @DisplayName("anular debe lanzar excepción si pago está aprobado")
    void anularDebeLanzarExcepcionSiPagoEstaAprobado() {
        Pago pago = pagoValido();
        pago.estado = "APROBADO";
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        PagoInvalidoException exception = assertThrows(
                PagoInvalidoException.class,
                () -> pagoService.anular(1L)
        );

        assertEquals("No se puede anular un pago aprobado", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }
}
