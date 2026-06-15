package cl.duoc.descuentoservice.repository;

import cl.duoc.descuentoservice.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescuentoRepository extends JpaRepository<Descuento, Long> {
}
