package cl.duoc.orderservice.service;

import cl.duoc.orderservice.client.InventoryClient;
import cl.duoc.orderservice.dto.InventarioResponse;
import cl.duoc.orderservice.exception.PedidoNotFoundException;
import cl.duoc.orderservice.model.Pedido;
import cl.duoc.orderservice.repository.PedidoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository pedidoRepository;
    private final InventoryClient inventoryClient;

    public PedidoService(PedidoRepository pedidoRepository, InventoryClient inventoryClient) {
        this.pedidoRepository = pedidoRepository;
        this.inventoryClient = inventoryClient;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarActivos() {
        logger.info("evento=listar_pedidos estado=activos");
        return pedidoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        logger.info("evento=listar_pedidos_por_usuario usuarioId={}", usuarioId);
        return pedidoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorEstado(String estado) {
        logger.info("evento=listar_pedidos_por_estado estado={}", estado);
        return pedidoRepository.findByEstadoAndActivoTrue(estado);
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        logger.info("evento=buscar_pedido id={}", id);
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("No se encontro pedido con id: " + id));
    }

    public Pedido crear(Pedido pedido) {
        validarPedido(pedido);

        InventarioResponse inventario = obtenerInventarioDesdeInventoryService(pedido.getSku());

        if (!Boolean.TRUE.equals(inventario.getActivo())) {
            throw new IllegalArgumentException("El inventario del producto esta inactivo");
        }

        if (inventario.getStockLibre() == null || inventario.getStockLibre() < pedido.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente para el SKU: " + pedido.getSku());
        }

        logger.info(
                "evento=validar_stock_pedido sku={} cantidadSolicitada={} stockLibre={} origen=order-service destino=inventory-service",
                pedido.getSku(),
                pedido.getCantidad(),
                inventario.getStockLibre()
        );

        inventoryClient.reservarStock(inventario.getId(), pedido.getCantidad());

        logger.info(
                "evento=reservar_stock_pedido sku={} cantidadReservada={} inventarioId={}",
                pedido.getSku(),
                pedido.getCantidad(),
                inventario.getId()
        );

        pedido.setId(null);
        pedido.setEstado("CREADO");
        pedido.setActivo(true);
        pedido.calcularTotal();

        Pedido guardado = pedidoRepository.save(pedido);

        logger.info(
                "evento=crear_pedido id={} usuarioId={} sku={} total={}",
                guardado.getId(),
                guardado.getUsuarioId(),
                guardado.getSku(),
                guardado.getTotal()
        );

        return guardado;
    }

    public Pedido cambiarEstado(Long id, String estado) {
        Pedido pedido = buscarPorId(id);

        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        pedido.setEstado(estado.toUpperCase());

        logger.info("evento=cambiar_estado_pedido id={} nuevoEstado={}", id, estado.toUpperCase());

        return pedidoRepository.save(pedido);
    }

    public Pedido cancelar(Long id) {
        Pedido pedido = buscarPorId(id);

        if ("PAGADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalArgumentException("No se puede cancelar un pedido pagado");
        }

        pedido.setEstado("CANCELADO");
        pedido.setActivo(false);

        logger.info("evento=cancelar_pedido id={}", id);

        return pedidoRepository.save(pedido);
    }

    private InventarioResponse obtenerInventarioDesdeInventoryService(String sku) {
        try {
            logger.info("evento=consultar_inventory_service sku={}", sku);
            return inventoryClient.buscarPorSku(sku);

        } catch (FeignException.NotFound ex) {
            logger.warn("evento=inventario_no_encontrado sku={}", sku);
            throw new IllegalArgumentException("No existe inventario para el SKU: " + sku);

        } catch (FeignException ex) {
            logger.error("evento=error_comunicacion_inventory_service sku={} status={}", sku, ex.status());
            throw new IllegalStateException("No fue posible comunicarse con inventory-service");
        }
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
