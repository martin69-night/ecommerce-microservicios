package cl.duoc.cartservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import cl.duoc.cartservice.model.CarritoItem;
import jakarta.validation.Valid;
import cl.duoc.cartservice.service.CarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Tag(name = "API Cart Service", description = "Documentación del carrito de compras")
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping
    public ResponseEntity<List<CarritoItem>> listarActivos() {
        return ResponseEntity.ok(carritoService.listarActivos());
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.listarPorUsuario(usuarioId));
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/{id}")
    public ResponseEntity<CarritoItem> buscarPorId(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(carritoService.buscarPorId(id));
    }

    @Operation(summary = "Crear registro", description = "Crea un nuevo recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PostMapping
    public ResponseEntity<CarritoItem> agregarItem(@Valid @RequestBody CarritoItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregarItem(item));
    }

    @Operation(summary = "Actualizar parcialmente", description = "Actualiza parcialmente un recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PatchMapping("/{id}/cantidad")
    public ResponseEntity<CarritoItem> actualizarCantidad(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(id, cantidad));
    }

    @Operation(summary = "Eliminar registro", description = "Elimina un recurso existente")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar registro", description = "Elimina un recurso existente")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/usuario/{usuarioId}/total")
    public ResponseEntity<Map<String, Object>> calcularTotal(@PathVariable Long usuarioId) {
        BigDecimal total = carritoService.calcularTotal(usuarioId);

        return ResponseEntity.ok(Map.of(
                "usuarioId", usuarioId,
                "total", total
        ));
    }
}
