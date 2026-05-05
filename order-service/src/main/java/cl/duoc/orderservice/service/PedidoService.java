package cl.duoc.orderservice.service;

import cl.duoc.orderservice.exception.PedidoNotFoundException;
import cl.duoc.orderservice.model.Pedido;
import cl.duoc.orderservice.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarActivos() {
        return pedidoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorEstado(String estado) {
        return pedidoRepository.findByEstadoAndActivoTrue(estado);
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("No se encontro pedido con id: " + id));
    }

    public Pedido crear(Pedido pedido) {
        validarPedido(pedido);

        pedido.setId(null);
        pedido.setEstado("CREADO");
        pedido.setActivo(true);
        pedido.calcularTotal();

        return pedidoRepository.save(pedido);
    }

    public Pedido cambiarEstado(Long id, String estado) {
        Pedido pedido = buscarPorId(id);

        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        pedido.setEstado(estado.toUpperCase());
        return pedidoRepository.save(pedido);
    }

    public Pedido cancelar(Long id) {
        Pedido pedido = buscarPorId(id);

        if ("PAGADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalArgumentException("No se puede cancelar un pedido pagado");
        }

        pedido.setEstado("CANCELADO");
        pedido.setActivo(false);

        return pedidoRepository.save(pedido);
    }

    private void validarPedido(Pedido pedido) {
        if (pedido.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuarioId es obligatorio");
        }

        if (pedido.getProductoId() == null) {
            throw new IllegalArgumentException("El productoId es obligatorio");
        }

        if (pedido.getSku() == null || pedido.getSku().isBlank()) {
            throw new IllegalArgumentException("El SKU es obligatorio");
        }

        if (pedido.getNombreProducto() == null || pedido.getNombreProducto().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (pedido.getPrecioUnitario() == null || pedido.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }

        if (pedido.getCantidad() == null || pedido.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }
}
