package cl.duoc.paymentservice.service;

import cl.duoc.paymentservice.exception.PagoInvalidoException;
import cl.duoc.paymentservice.exception.PagoNotFoundException;
import cl.duoc.paymentservice.model.Pago;
import cl.duoc.paymentservice.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pago> listarActivos() {
        return pagoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPorUsuario(Long usuarioId) {
        return pagoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPorEstado(String estado) {
        return pagoRepository.findByEstadoAndActivoTrue(estado.toUpperCase());
    }

    @Transactional(readOnly = true)
    public Pago buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNotFoundException("No se encontro pago con id: " + id));
    }

    @Transactional(readOnly = true)
    public Pago buscarPorPedidoId(Long pedidoId) {
        return pagoRepository.findByPedidoIdAndActivoTrue(pedidoId)
                .orElseThrow(() -> new PagoNotFoundException("No se encontro pago para pedidoId: " + pedidoId));
    }

    public Pago crear(Pago pago) {
        validarPago(pago);

        pago.id = null;
        pago.estado = "PENDIENTE";
        pago.activo = true;

        return pagoRepository.save(pago);
    }

    public Pago aprobar(Long id) {
        Pago pago = buscarPorId(id);

        if (!"PENDIENTE".equalsIgnoreCase(pago.estado)) {
            throw new PagoInvalidoException("Solo se pueden aprobar pagos pendientes");
        }

        pago.estado = "APROBADO";
        pago.codigoTransaccion = "TX-" + UUID.randomUUID();

        return pagoRepository.save(pago);
    }

    public Pago rechazar(Long id) {
        Pago pago = buscarPorId(id);

        if (!"PENDIENTE".equalsIgnoreCase(pago.estado)) {
            throw new PagoInvalidoException("Solo se pueden rechazar pagos pendientes");
        }

        pago.estado = "RECHAZADO";

        return pagoRepository.save(pago);
    }

    public Pago anular(Long id) {
        Pago pago = buscarPorId(id);

        if ("APROBADO".equalsIgnoreCase(pago.estado)) {
            throw new PagoInvalidoException("No se puede anular un pago aprobado");
        }

        pago.estado = "ANULADO";
        pago.activo = false;

        return pagoRepository.save(pago);
    }

    private void validarPago(Pago pago) {
        if (pago.pedidoId == null) {
            throw new IllegalArgumentException("El pedidoId es obligatorio");
        }

        if (pago.usuarioId == null) {
            throw new IllegalArgumentException("El usuarioId es obligatorio");
        }

        if (pago.monto == null || pago.monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        if (pago.metodoPago == null || pago.metodoPago.isBlank()) {
            throw new IllegalArgumentException("El metodo de pago es obligatorio");
        }
    }
}
