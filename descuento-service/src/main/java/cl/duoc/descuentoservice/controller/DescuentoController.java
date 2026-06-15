package cl.duoc.descuentoservice.controller;

import cl.duoc.descuentoservice.dto.DescuentoRequest;
import cl.duoc.descuentoservice.dto.DescuentoResponse;
import cl.duoc.descuentoservice.service.DescuentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descuentos")
public class DescuentoController {

    private final DescuentoService service;

    public DescuentoController(DescuentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DescuentoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescuentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DescuentoResponse> crear(@Valid @RequestBody DescuentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DescuentoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody DescuentoRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
