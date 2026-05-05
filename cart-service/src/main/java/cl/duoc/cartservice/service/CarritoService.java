package cl.duoc.cartservice.service;

import cl.duoc.cartservice.exception.CarritoItemNotFoundException;
import cl.duoc.cartservice.exception.CarritoVacioException;
import cl.duoc.cartservice.model.CarritoItem;
import cl.duoc.cartservice.repository.CarritoItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CarritoService {

    private final CarritoItemRepository carritoItemRepository;

    public CarritoService(CarritoItemRepository carritoItemRepository) {
        this.carritoItemRepository = carritoItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CarritoItem> listarActivos() {
        return carritoItemRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<CarritoItem> listarPorUsuario(Long usuarioId) {
        return carritoItemRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }

    @Transactional(readOnly = true)
    public CarritoItem buscarPorId(Long id) {
        return carritoItemRepository.findById(id)
                .orElseThrow(() -> new CarritoItemNotFoundException("No se encontro item de carrito con id: " + id));
    }

    public CarritoItem agregarItem(CarritoItem item) {
        validarItem(item);

        return carritoItemRepository
                .findByUsuarioIdAndSkuAndActivoTrue(item.getUsuarioId(), item.getSku())
                .map(existente -> {
                    existente.setCantidad(existente.getCantidad() + item.getCantidad());
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

        return carritoItemRepository.save(item);
    }

    public void eliminarItem(Long id) {
        CarritoItem item = buscarPorId(id);
        item.setActivo(false);
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

        carritoItemRepository.saveAll(items);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotal(Long usuarioId) {
        List<CarritoItem> items = carritoItemRepository.findByUsuarioIdAndActivoTrue(usuarioId);

        return items.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validarItem(CarritoItem item) {
        if (item.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuarioId es obligatorio");
        }

        if (item.getProductoId() == null) {
            throw new IllegalArgumentException("El productoId es obligatorio");
        }

        if (item.getSku() == null || item.getSku().isBlank()) {
            throw new IllegalArgumentException("El SKU es obligatorio");
        }

        if (item.getNombreProducto() == null || item.getNombreProducto().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (item.getPrecioUnitario() == null || item.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }

        validarCantidad(item.getCantidad());
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }
}
