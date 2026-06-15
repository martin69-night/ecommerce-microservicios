package cl.duoc.usuarioservice.service;

import cl.duoc.usuarioservice.dto.UsuarioRequest;
import cl.duoc.usuarioservice.dto.UsuarioResponse;
import cl.duoc.usuarioservice.exception.RecursoNoEncontradoException;
import cl.duoc.usuarioservice.model.Usuario;
import cl.duoc.usuarioservice.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UsuarioResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
    }

    @Transactional
    public UsuarioResponse crear(UsuarioRequest dto) {

        Usuario entity = new Usuario();
        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        entity.setRol(dto.getRol());
        entity.setActivo(dto.getActivo());
        entity.setFechaCreacion(LocalDateTime.now());

        Usuario guardado = repository.save(entity);
        log.info("Usuario creado con id {}", guardado.getId());
        return toResponse(guardado);
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest dto) {

        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));

        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        entity.setRol(dto.getRol());
        entity.setActivo(dto.getActivo());

        Usuario actualizado = repository.save(entity);
        log.info("Usuario actualizado con id {}", actualizado.getId());
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con id " + id);
        }
        repository.deleteById(id);
        log.info("Usuario eliminado con id {}", id);
    }

    private UsuarioResponse toResponse(Usuario entity) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setRol(entity.getRol());
        dto.setActivo(entity.getActivo());
        dto.setFechaCreacion(entity.getFechaCreacion());

        return dto;
    }
}
