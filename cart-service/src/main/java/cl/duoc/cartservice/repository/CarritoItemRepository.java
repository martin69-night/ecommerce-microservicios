package cl.duoc.cartservice.repository;

import cl.duoc.cartservice.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByActivoTrue();

    List<CarritoItem> findByUsuarioIdAndActivoTrue(Long usuarioId);

    Optional<CarritoItem> findByUsuarioIdAndSkuAndActivoTrue(Long usuarioId, String sku);

    Optional<CarritoItem> findByUsuarioIdAndProductoIdAndActivoTrue(Long usuarioId, Long productoId);
}
