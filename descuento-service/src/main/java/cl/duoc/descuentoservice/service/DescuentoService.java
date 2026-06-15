package cl.duoc.descuentoservice.service;

import cl.duoc.descuentoservice.dto.DescuentoRequest;
import cl.duoc.descuentoservice.dto.DescuentoResponse;
import cl.duoc.descuentoservice.exception.RecursoNoEncontradoException;
import cl.duoc.descuentoservice.model.Descuento;
import cl.duoc.descuentoservice.repository.DescuentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DescuentoService {

    private static final Logger log = LoggerFactory.getLogger(DescuentoService.class);

    private final DescuentoRepository repository;

    public DescuentoService(DescuentoRepository repository) {
        this.repository = repository;
    }

    public List<DescuentoResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public DescuentoResponse buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Descuento no encontrado con id " + id));
    }

    @Transactional
    public DescuentoResponse crear(DescuentoRequest dto) {

        Descuento entity = new Descuento();
        entity.setCodigo(dto.getCodigo());
        entity.setPorcentaje(dto.getPorcentaje());
        entity.setActivo(dto.getActivo());
        entity.setFechaCreacion(LocalDateTime.now());

        Descuento guardado = repository.save(entity);
        log.info("Descuento creado con id {}", guardado.getId());
        return toResponse(guardado);
    }

    @Transactional
    public DescuentoResponse actualizar(Long id, DescuentoRequest dto) {

        Descuento entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Descuento no encontrado con id " + id));

        entity.setCodigo(dto.getCodigo());
        entity.setPorcentaje(dto.getPorcentaje());
        entity.setActivo(dto.getActivo());

        Descuento actualizado = repository.save(entity);
        log.info("Descuento actualizado con id {}", actualizado.getId());
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Descuento no encontrado con id " + id);
        }
        repository.deleteById(id);
        log.info("Descuento eliminado con id {}", id);
    }

    private DescuentoResponse toResponse(Descuento entity) {
        DescuentoResponse dto = new DescuentoResponse();
        dto.setId(entity.getId());
        dto.setCodigo(entity.getCodigo());
        dto.setPorcentaje(entity.getPorcentaje());
        dto.setActivo(entity.getActivo());
        dto.setFechaCreacion(entity.getFechaCreacion());

        return dto;
    }
}
