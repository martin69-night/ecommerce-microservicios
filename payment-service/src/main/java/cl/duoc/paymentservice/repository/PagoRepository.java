package cl.duoc.paymentservice.repository;

import cl.duoc.paymentservice.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByActivoTrue();

    List<Pago> findByUsuarioIdAndActivoTrue(Long usuarioId);

    List<Pago> findByEstadoAndActivoTrue(String estado);

    Optional<Pago> findByPedidoIdAndActivoTrue(Long pedidoId);
}
