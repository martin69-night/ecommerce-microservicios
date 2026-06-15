package cl.duoc.notificacionservice.service;

import cl.duoc.notificacionservice.dto.NotificacionRequest;
import cl.duoc.notificacionservice.dto.NotificacionResponse;
import cl.duoc.notificacionservice.exception.RecursoNoEncontradoException;
import cl.duoc.notificacionservice.model.Notificacion;
import cl.duoc.notificacionservice.repository.NotificacionRepository;
import cl.duoc.notificacionservice.client.UsuarioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository repository;
    private final UsuarioClient usuarioClient;

    public NotificacionService(NotificacionRepository repository, UsuarioClient usuarioClient) {
        this.repository = repository;
        this.usuarioClient = usuarioClient;
    }

    public List<NotificacionResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public NotificacionResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificacion no encontrado con id " + id));
    }

    @Transactional
    public NotificacionResponse crear(NotificacionRequest dto) {

        validarUsuario(dto.getUsuarioId());

        Notificacion entity = new Notificacion();
        entity.setUsuarioId(dto.getUsuarioId());
        entity.setCanal(dto.getCanal());
        entity.setMensaje(dto.getMensaje());
        entity.setEstado(dto.getEstado());
        entity.setFechaCreacion(LocalDateTime.now());

        Notificacion guardado = repository.save(entity);
        log.info("Notificacion creado con id {}", guardado.getId());
        return toResponse(guardado);
    }

    @Transactional
    public NotificacionResponse actualizar(Long id, NotificacionRequest dto) {

        validarUsuario(dto.getUsuarioId());

        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificacion no encontrado con id " + id));

        entity.setUsuarioId(dto.getUsuarioId());
        entity.setCanal(dto.getCanal());
        entity.setMensaje(dto.getMensaje());
        entity.setEstado(dto.getEstado());

        Notificacion actualizado = repository.save(entity);
        log.info("Notificacion actualizado con id {}", actualizado.getId());
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Notificacion no encontrado con id " + id);
        }
        repository.deleteById(id);
        log.info("Notificacion eliminado con id {}", id);
    }

    private void validarUsuario(Long usuarioId) {
        try {
            usuarioClient.buscarPorId(usuarioId);
        } catch (Exception ex) {
            throw new IllegalArgumentException("El usuario indicado no existe o no esta disponible");
        }
    }

    private NotificacionResponse toResponse(Notificacion entity) {
        NotificacionResponse dto = new NotificacionResponse();
        dto.setId(entity.getId());
        dto.setUsuarioId(entity.getUsuarioId());
        dto.setCanal(entity.getCanal());
        dto.setMensaje(entity.getMensaje());
        dto.setEstado(entity.getEstado());
        dto.setFechaCreacion(entity.getFechaCreacion());

        return dto;
    }
}
