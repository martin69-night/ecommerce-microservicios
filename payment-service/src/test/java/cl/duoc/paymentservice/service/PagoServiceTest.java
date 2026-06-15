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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService - Capa Servicio")
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    @Test
    @DisplayName("listarActivos debe retornar lista de pagos activos")
    void listarActivosDebeRetornarLista() {
        // Given
        Pago p = new Pago();
        p.id = 1L;
        p.estado = "PENDIENTE";
        when(pagoRepository.findByActivoTrue()).thenReturn(List.of(p));
        // When
        List<Pago> resultado = pagoService.listarActivos();
        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pagoRepository, times(1)).findByActivoTrue();
    }

    @Test
    @DisplayName("buscarPorId debe retornar pago cuando existe")
    void buscarPorIdDebeRetornarPagoCuandoExiste() {
        // Given
        Pago p = new Pago();
        p.id = 1L;
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(p));
        // When
        Pago resultado = pagoService.buscarPorId(1L);
        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.id);
    }

    @Test
    @DisplayName("buscarPorId debe lanzar excepción cuando no existe")
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        // Given
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());
        // When / Then
        assertThrows(PagoNotFoundException.class, () -> pagoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("aprobar debe lanzar excepción si pago no está pendiente (regla de negocio)")
    void aprobarDebeLanzarExcepcionSiNoEstaPendiente() {
        // Given
        Pago p = new Pago();
        p.id = 1L;
        p.estado = "APROBADO";
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(p));
        // When / Then
        assertThrows(PagoInvalidoException.class, () -> pagoService.aprobar(1L));
    }

    @Test
    @DisplayName("crear debe lanzar excepción si monto es cero (regla de negocio)")
    void crearDebeLanzarExcepcionSiMontoEsCero() {
        // Given
        Pago p = new Pago();
        p.pedidoId = 1L;
        p.usuarioId = 2L;
        p.monto = BigDecimal.ZERO;
        p.metodoPago = "TARJETA";
        // When / Then
        assertThrows(IllegalArgumentException.class, () -> pagoService.crear(p));
    }

    @Test
    @DisplayName("anular debe lanzar excepción si pago está aprobado (regla de negocio)")
    void anularDebeLanzarExcepcionSiPagoEstaAprobado() {
        // Given
        Pago p = new Pago();
        p.id = 1L;
        p.estado = "APROBADO";
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(p));
        // When / Then
        assertThrows(PagoInvalidoException.class, () -> pagoService.anular(1L));
    }
}
