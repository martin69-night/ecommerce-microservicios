package cl.duoc.inventoryservice.service;

import cl.duoc.inventoryservice.exception.InventarioNotFoundException;
import cl.duoc.inventoryservice.exception.SkuInventarioDuplicadoException;
import cl.duoc.inventoryservice.exception.StockInsuficienteException;
import cl.duoc.inventoryservice.model.Inventario;
import cl.duoc.inventoryservice.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Inventario> listarActivos() {
        return inventarioRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Inventario buscarPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new InventarioNotFoundException("No se encontro inventario con id: " + id));
    }

    @Transactional(readOnly = true)
    public Inventario buscarPorSku(String sku) {
        return inventarioRepository.findBySku(sku)
                .orElseThrow(() -> new InventarioNotFoundException("No se encontro inventario con sku: " + sku));
    }

    @Transactional(readOnly = true)
    public Inventario buscarPorProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new InventarioNotFoundException("No se encontro inventario para productoId: " + productoId));
    }

    public Inventario crear(Inventario inventario) {
        if (inventarioRepository.existsBySku(inventario.getSku())) {
            throw new SkuInventarioDuplicadoException("Ya existe inventario con SKU: " + inventario.getSku());
        }

        inventario.setId(null);

        if (inventario.getCantidadDisponible() == null) {
            inventario.setCantidadDisponible(0);
        }

        if (inventario.getCantidadReservada() == null) {
            inventario.setCantidadReservada(0);
        }

        if (inventario.getActivo() == null) {
            inventario.setActivo(true);
        }

        return inventarioRepository.save(inventario);
    }

    public Inventario actualizar(Long id, Inventario datos) {
        Inventario existente = buscarPorId(id);

        if (!existente.getSku().equals(datos.getSku()) && inventarioRepository.existsBySku(datos.getSku())) {
            throw new SkuInventarioDuplicadoException("Ya existe otro inventario con SKU: " + datos.getSku());
        }

        existente.setProductoId(datos.getProductoId());
        existente.setSku(datos.getSku());
        existente.setCantidadDisponible(datos.getCantidadDisponible());
        existente.setCantidadReservada(datos.getCantidadReservada());
        existente.setUbicacion(datos.getUbicacion());
        existente.setActivo(datos.getActivo());

        return inventarioRepository.save(existente);
    }

    public Inventario reservarStock(Long id, Integer cantidad) {
        validarCantidad(cantidad);

        Inventario inventario = buscarPorId(id);

        if (inventario.getStockLibre() < cantidad) {
            throw new StockInsuficienteException("No hay stock libre suficiente para reservar");
        }

        inventario.setCantidadReservada(inventario.getCantidadReservada() + cantidad);
        return inventarioRepository.save(inventario);
    }

    public Inventario liberarReserva(Long id, Integer cantidad) {
        validarCantidad(cantidad);

        Inventario inventario = buscarPorId(id);

        if (inventario.getCantidadReservada() < cantidad) {
            throw new IllegalArgumentException("No se puede liberar mas stock del reservado");
        }

        inventario.setCantidadReservada(inventario.getCantidadReservada() - cantidad);
        return inventarioRepository.save(inventario);
    }

    public Inventario descontarStock(Long id, Integer cantidad) {
        validarCantidad(cantidad);

        Inventario inventario = buscarPorId(id);

        if (inventario.getCantidadDisponible() < cantidad) {
            throw new StockInsuficienteException("No hay stock suficiente para descontar");
        }

        inventario.setCantidadDisponible(inventario.getCantidadDisponible() - cantidad);

        if (inventario.getCantidadReservada() >= cantidad) {
            inventario.setCantidadReservada(inventario.getCantidadReservada() - cantidad);
        }

        return inventarioRepository.save(inventario);
    }

    public Inventario reponerStock(Long id, Integer cantidad) {
        validarCantidad(cantidad);

        Inventario inventario = buscarPorId(id);
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() + cantidad);

        return inventarioRepository.save(inventario);
    }

    public void desactivar(Long id) {
        Inventario inventario = buscarPorId(id);
        inventario.setActivo(false);
        inventarioRepository.save(inventario);
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }
}
