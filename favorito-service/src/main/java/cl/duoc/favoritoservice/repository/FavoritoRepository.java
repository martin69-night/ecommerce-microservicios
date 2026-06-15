package cl.duoc.favoritoservice.repository;

import cl.duoc.favoritoservice.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
}
