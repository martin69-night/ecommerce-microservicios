package cl.duoc.envioservice.repository;

import cl.duoc.envioservice.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioRepository extends JpaRepository<Envio, Long> {
}
