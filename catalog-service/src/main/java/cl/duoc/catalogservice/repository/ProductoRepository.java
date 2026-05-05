package cl.duoc.catalogservice.repository;

import cl.duoc.catalogservice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoriaAndActivoTrue(String categoria);

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
