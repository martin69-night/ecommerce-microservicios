package cl.duoc.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import cl.duoc.orderservice.model.Pedido;
import jakarta.validation.Valid;
import cl.duoc.orderservice.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "API Order Service", description = "Documentación de pedidos")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping
    public ResponseEntity<List<Pedido>> listarActivos() {
        return ResponseEntity.ok(pedidoService.listarActivos());
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.listarPorEstado(estado.toUpperCase()));
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @Operation(summary = "Crear registro", description = "Crea un nuevo recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PostMapping
    public ResponseEntity<Pedido> crear(@Valid @RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(pedido));
    }

    @Operation(summary = "Actualizar parcialmente", description = "Actualiza parcialmente un recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, estado));
    }

    @Operation(summary = "Actualizar parcialmente", description = "Actualiza parcialmente un recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelar(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }
}
