package cl.duoc.orderservice.repository;

import cl.duoc.orderservice.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByActivoTrue();

    List<Pedido> findByUsuarioIdAndActivoTrue(Long usuarioId);

    List<Pedido> findByEstadoAndActivoTrue(String estado);
}
