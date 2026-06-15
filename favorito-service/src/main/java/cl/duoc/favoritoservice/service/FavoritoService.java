package cl.duoc.favoritoservice.service;

import cl.duoc.favoritoservice.dto.FavoritoRequest;
import cl.duoc.favoritoservice.dto.FavoritoResponse;
import cl.duoc.favoritoservice.exception.RecursoNoEncontradoException;
import cl.duoc.favoritoservice.model.Favorito;
import cl.duoc.favoritoservice.repository.FavoritoRepository;
import cl.duoc.favoritoservice.client.UsuarioClient;
import cl.duoc.favoritoservice.client.CatalogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoritoService {

    private static final Logger log = LoggerFactory.getLogger(FavoritoService.class);

    private final FavoritoRepository repository;
    private final UsuarioClient usuarioClient;
    private final CatalogClient catalogClient;

    public FavoritoService(FavoritoRepository repository, UsuarioClient usuarioClient, CatalogClient catalogClient) {
        this.repository = repository;
        this.usuarioClient = usuarioClient;
        this.catalogClient = catalogClient;
    }

    public List<FavoritoResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public FavoritoResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Favorito no encontrado con id " + id));
    }

    @Transactional
    public FavoritoResponse crear(FavoritoRequest dto) {

        validarUsuario(dto.getUsuarioId());

        validarProducto(dto.getProductoId());

        Favorito entity = new Favorito();
        entity.setUsuarioId(dto.getUsuarioId());
        entity.setProductoId(dto.getProductoId());
        entity.setSku(dto.getSku());
        entity.setFechaCreacion(LocalDateTime.now());

        Favorito guardado = repository.save(entity);
        log.info("Favorito creado con id {}", guardado.getId());
        return toResponse(guardado);
    }

    @Transactional
    public FavoritoResponse actualizar(Long id, FavoritoRequest dto) {

        validarUsuario(dto.getUsuarioId());

        validarProducto(dto.getProductoId());

        Favorito entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Favorito no encontrado con id " + id));

        entity.setUsuarioId(dto.getUsuarioId());
        entity.setProductoId(dto.getProductoId());
        entity.setSku(dto.getSku());

        Favorito actualizado = repository.save(entity);
        log.info("Favorito actualizado con id {}", actualizado.getId());
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Favorito no encontrado con id " + id);
        }
        repository.deleteById(id);
        log.info("Favorito eliminado con id {}", id);
    }

    private void validarUsuario(Long usuarioId) {
        try {
            usuarioClient.buscarPorId(usuarioId);
        } catch (Exception ex) {
            throw new IllegalArgumentException("El usuario indicado no existe o no esta disponible");
        }
    }

    private void validarProducto(Long productoId) {
        try {
            catalogClient.buscarPorId(productoId);
        } catch (Exception ex) {
            throw new IllegalArgumentException("El producto indicado no existe o no esta disponible");
        }
    }

    private FavoritoResponse toResponse(Favorito entity) {
        FavoritoResponse dto = new FavoritoResponse();
        dto.setId(entity.getId());
        dto.setUsuarioId(entity.getUsuarioId());
        dto.setProductoId(entity.getProductoId());
        dto.setSku(entity.getSku());
        dto.setFechaCreacion(entity.getFechaCreacion());

        return dto;
    }
}
