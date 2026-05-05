package cl.duoc.inventoryservice.repository;

import cl.duoc.inventoryservice.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findBySku(String sku);

    Optional<Inventario> findByProductoId(Long productoId);

    boolean existsBySku(String sku);

    List<Inventario> findByActivoTrue();

    List<Inventario> findByCantidadDisponibleLessThanAndActivoTrue(Integer cantidadDisponible);
}
