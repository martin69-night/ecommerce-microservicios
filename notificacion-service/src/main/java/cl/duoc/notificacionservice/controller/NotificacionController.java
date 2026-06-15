package cl.duoc.notificacionservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import cl.duoc.notificacionservice.dto.NotificacionRequest;
import cl.duoc.notificacionservice.dto.NotificacionResponse;
import cl.duoc.notificacionservice.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "API Notificacion Service", description = "Documentación de notificaciones")
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService service;

    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponse> buscarPorId(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear registro", description = "Crea un nuevo recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PostMapping
    public ResponseEntity<NotificacionResponse> crear(@Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @Operation(summary = "Actualizar registro", description = "Actualiza un recurso existente")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PutMapping("/{id}")
    public ResponseEntity<NotificacionResponse> actualizar(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id, @Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar registro", description = "Elimina un recurso existente")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
