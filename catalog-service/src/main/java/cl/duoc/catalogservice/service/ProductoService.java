package cl.duoc.catalogservice.service;

import cl.duoc.catalogservice.exception.ProductoNotFoundException;
import cl.duoc.catalogservice.exception.SkuDuplicadoException;
import cl.duoc.catalogservice.model.Producto;
import cl.duoc.catalogservice.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("No se encontro el producto con id: " + id));
    }

    @Transactional(readOnly = true)
    public Producto buscarPorSku(String sku) {
        return productoRepository.findBySku(sku)
                .orElseThrow(() -> new ProductoNotFoundException("No se encontro el producto con sku: " + sku));
    }

    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }

    public Producto crear(Producto producto) {
        if (productoRepository.existsBySku(producto.getSku())) {
            throw new SkuDuplicadoException("Ya existe un producto con SKU: " + producto.getSku());
        }

        producto.setId(null);

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto datosActualizados) {
        Producto existente = buscarPorId(id);

        if (!existente.getSku().equals(datosActualizados.getSku())) {
            if (productoRepository.existsBySku(datosActualizados.getSku())) {
                throw new SkuDuplicadoException("Ya existe otro producto con SKU: " + datosActualizados.getSku());
            }
        }

        existente.setSku(datosActualizados.getSku());
        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        existente.setPrecio(datosActualizados.getPrecio());
        existente.setCategoria(datosActualizados.getCategoria());
        existente.setActivo(datosActualizados.getActivo());

        return productoRepository.save(existente);
    }

    public void desactivar(Long id) {
        Producto producto = buscarPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public Producto reactivar(Long id) {
        Producto producto = buscarPorId(id);
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    public void eliminarFisicamente(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException("No se encontro el producto con id: " + id);
        }

        productoRepository.deleteById(id);
    }
}
