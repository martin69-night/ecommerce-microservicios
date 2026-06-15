package cl.duoc.envioservice.service;

import cl.duoc.envioservice.dto.EnvioRequest;
import cl.duoc.envioservice.dto.EnvioResponse;
import cl.duoc.envioservice.exception.RecursoNoEncontradoException;
import cl.duoc.envioservice.model.Envio;
import cl.duoc.envioservice.repository.EnvioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvioService {

    private static final Logger log = LoggerFactory.getLogger(EnvioService.class);

    private final EnvioRepository repository;

    public EnvioService(EnvioRepository repository) {
        this.repository = repository;
    }

    public List<EnvioResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public EnvioResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Envio no encontrado con id " + id));
    }

    @Transactional
    public EnvioResponse crear(EnvioRequest dto) {

        Envio entity = new Envio();
        entity.setPedidoId(dto.getPedidoId());
        entity.setDireccion(dto.getDireccion());
        entity.setCiudad(dto.getCiudad());
        entity.setEstado(dto.getEstado());
        entity.setFechaCreacion(LocalDateTime.now());

        Envio guardado = repository.save(entity);
        log.info("Envio creado con id {}", guardado.getId());
        return toResponse(guardado);
    }

    @Transactional
    public EnvioResponse actualizar(Long id, EnvioRequest dto) {

        Envio entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Envio no encontrado con id " + id));

        entity.setPedidoId(dto.getPedidoId());
        entity.setDireccion(dto.getDireccion());
        entity.setCiudad(dto.getCiudad());
        entity.setEstado(dto.getEstado());

        Envio actualizado = repository.save(entity);
        log.info("Envio actualizado con id {}", actualizado.getId());
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Envio no encontrado con id " + id);
        }
        repository.deleteById(id);
        log.info("Envio eliminado con id {}", id);
    }

    private EnvioResponse toResponse(Envio entity) {
        EnvioResponse dto = new EnvioResponse();
        dto.setId(entity.getId());
        dto.setPedidoId(entity.getPedidoId());
        dto.setDireccion(entity.getDireccion());
        dto.setCiudad(entity.getCiudad());
        dto.setEstado(entity.getEstado());
        dto.setFechaCreacion(entity.getFechaCreacion());

        return dto;
    }
}
