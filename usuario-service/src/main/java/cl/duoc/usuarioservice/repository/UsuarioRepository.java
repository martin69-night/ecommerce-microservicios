package cl.duoc.usuarioservice.repository;

import cl.duoc.usuarioservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
