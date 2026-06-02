package cl.duoc.cartservice.service;

import cl.duoc.cartservice.client.CatalogClient;
import cl.duoc.cartservice.dto.ProductoResponse;
import cl.duoc.cartservice.exception.CarritoItemNotFoundException;
import cl.duoc.cartservice.exception.CarritoVacioException;
import cl.duoc.cartservice.model.CarritoItem;
import cl.duoc.cartservice.repository.CarritoItemRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CarritoService {

    private static final Logger logger = LoggerFactory.getLogger(CarritoService.class);

    private final CarritoItemRepository carritoItemRepository;
    private final CatalogClient catalogClient;

    public CarritoService(CarritoItemRepository carritoItemRepository, CatalogClient catalogClient) {
        this.carritoItemRepository = carritoItemRepository;
        this.catalogClient = catalogClient;
    }

    @Transactional(readOnly = true)
    public List<CarritoItem> listarActivos() {
        logger.info("evento=listar_items_carrito estado=activos");
        return carritoItemRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<CarritoItem> listarPorUsuario(Long usuarioId) {
        logger.info("evento=listar_carrito_por_usuario usuarioId={}", usuarioId);
        return carritoItemRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }

    @Transactional(readOnly = true)
    public CarritoItem buscarPorId(Long id) {
        logger.info("evento=buscar_item_carrito id={}", id);
        return carritoItemRepository.findById(id)
                .orElseThrow(() -> new CarritoItemNotFoundException("No se encontro item de carrito con id: " + id));
    }

    public CarritoItem agregarItem(CarritoItem item) {
        validarSolicitudCarrito(item);

        ProductoResponse producto = obtenerProductoDesdeCatalogo(item.getSku());

        item.setProductoId(producto.getId());
        item.setNombreProducto(producto.getNombre());
        item.setPrecioUnitario(producto.getPrecio());

        logger.info(
                "evento=agregar_item_carrito usuarioId={} sku={} origen=cart-service destino=catalog-service",
                item.getUsuarioId(),
                item.getSku()
        );

        return carritoItemRepository
                .findByUsuarioIdAndSkuAndActivoTrue(item.getUsuarioId(), item.getSku())
                .map(existente -> {
                    existente.setCantidad(existente.getCantidad() + item.getCantidad());
                    existente.setProductoId(producto.getId());
                    existente.setNombreProducto(producto.getNombre());
                    existente.setPrecioUnitario(producto.getPrecio());
                    return carritoItemRepository.save(existente);
                })
                .orElseGet(() -> {
                    item.setId(null);

                    if (item.getActivo() == null) {
                        item.setActivo(true);
                    }

                    return carritoItemRepository.save(item);
                });
    }

    public CarritoItem actualizarCantidad(Long id, Integer cantidad) {
        validarCantidad(cantidad);

        CarritoItem item = buscarPorId(id);
        item.setCantidad(cantidad);

        logger.info("evento=actualizar_cantidad_carrito id={} cantidad={}", id, cantidad);

        return carritoItemRepository.save(item);
    }

    public void eliminarItem(Long id) {
        CarritoItem item = buscarPorId(id);
        item.setActivo(false);

        logger.info("evento=eliminar_item_carrito id={}", id);

        carritoItemRepository.save(item);
    }

    public void vaciarCarrito(Long usuarioId) {
        List<CarritoItem> items = carritoItemRepository.findByUsuarioIdAndActivoTrue(usuarioId);

        if (items.isEmpty()) {
            throw new CarritoVacioException("El usuario no tiene items activos en el carrito");
        }

        for (CarritoItem item : items) {
            item.setActivo(false);
        }

        logger.info("evento=vaciar_carrito usuarioId={} cantidadItems={}", usuarioId, items.size());

        carritoItemRepository.saveAll(items);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotal(Long usuarioId) {
        List<CarritoItem> items = carritoItemRepository.findByUsuarioIdAndActivoTrue(usuarioId);

        BigDecimal total = items.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        logger.info("evento=calcular_total_carrito usuarioId={} total={}", usuarioId, total);

        return total;
    }

    private ProductoResponse obtenerProductoDesdeCatalogo(String sku) {
        try {
            logger.info("evento=consultar_catalog_service sku={}", sku);

            ProductoResponse producto = catalogClient.buscarPorSku(sku);

            if (producto == null) {
                throw new IllegalArgumentException("El producto no existe en catalog-service");
            }

            if (!Boolean.TRUE.equals(producto.getActivo())) {
                throw new IllegalArgumentException("El producto existe pero esta inactivo en catalog-service");
            }

            if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El producto no tiene precio valido en catalog-service");
            }

            logger.info("evento=producto_recibido_desde_catalog_service sku={} productoId={} nombre={}",
                    producto.getSku(),
                    producto.getId(),
                    producto.getNombre()
            );

            return producto;

        } catch (FeignException.NotFound ex) {
            logger.warn("evento=producto_no_encontrado_en_catalog_service sku={}", sku);
            throw new IllegalArgumentException("No existe un producto con SKU: " + sku);
        } catch (FeignException ex) {
            logger.error("evento=error_comunicacion_catalog_service sku={} status={}", sku, ex.status());
            throw new IllegalStateException("No fue posible comunicarse con catalog-service");
        }
    }

    private void validarSolicitudCarrito(CarritoItem item) {
        if (item.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuarioId es obligatorio");
        }

        if (item.getSku() == null || item.getSku().isBlank()) {
            throw new IllegalArgumentException("El SKU es obligatorio");
        }

        validarCantidad(item.getCantidad());
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }
}
