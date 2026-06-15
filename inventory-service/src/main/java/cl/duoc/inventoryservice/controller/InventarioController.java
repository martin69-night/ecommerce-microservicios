package cl.duoc.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import cl.duoc.inventoryservice.model.Inventario;
import cl.duoc.inventoryservice.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "API Inventory Service", description = "Documentación del inventario")
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping
    public ResponseEntity<List<Inventario>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarActivos());
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/todos")
    public ResponseEntity<List<Inventario>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Inventario> buscarPorSku(@PathVariable String sku) {
        return ResponseEntity.ok(inventarioService.buscarPorSku(sku));
    }

    @Operation(summary = "Consultar registros", description = "Obtiene información del recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Inventario> buscarPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarPorProductoId(productoId));
    }

    @Operation(summary = "Crear registro", description = "Crea un nuevo recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario inventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crear(inventario));
    }

    @Operation(summary = "Actualizar registro", description = "Actualiza un recurso existente")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id, @RequestBody Inventario inventario) {
        return ResponseEntity.ok(inventarioService.actualizar(id, inventario));
    }

    @Operation(summary = "Actualizar parcialmente", description = "Actualiza parcialmente un recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PatchMapping("/{id}/reservar")
    public ResponseEntity<Inventario> reservarStock(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.reservarStock(id, cantidad));
    }

    @Operation(summary = "Actualizar parcialmente", description = "Actualiza parcialmente un recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Inventario> liberarReserva(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.liberarReserva(id, cantidad));
    }

    @Operation(summary = "Actualizar parcialmente", description = "Actualiza parcialmente un recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PatchMapping("/{id}/descontar")
    public ResponseEntity<Inventario> descontarStock(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.descontarStock(id, cantidad));
    }

    @Operation(summary = "Actualizar parcialmente", description = "Actualiza parcialmente un recurso")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @PatchMapping("/{id}/reponer")
    public ResponseEntity<Inventario> reponerStock(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.reponerStock(id, cantidad));
    }

    @Operation(summary = "Eliminar registro", description = "Elimina un recurso existente")

    @ApiResponses({

            @ApiResponse(responseCode = "200", description = "Operación exitosa"),

            @ApiResponse(responseCode = "201", description = "Recurso creado"),

            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),

            @ApiResponse(responseCode = "404", description = "Recurso no encontrado")

    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@Parameter(description = "ID del recurso", required = true) @PathVariable Long id) {
        inventarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
