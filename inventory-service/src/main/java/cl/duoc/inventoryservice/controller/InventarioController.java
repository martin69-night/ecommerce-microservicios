package cl.duoc.inventoryservice.controller;

import cl.duoc.inventoryservice.model.Inventario;
import cl.duoc.inventoryservice.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Inventario",
        description = "Operaciones para administrar stock, reservas y reposición de productos"
)
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(
            summary = "Listar inventarios activos",
            description = "Obtiene los registros de inventario que se encuentran activos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventarios activos obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<Inventario>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarActivos());
    }

    @Operation(
            summary = "Listar todos los inventarios",
            description = "Obtiene todos los registros de inventario, incluyendo los que están desactivados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventarios obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            )
    })
    @GetMapping("/todos")
    public ResponseEntity<List<Inventario>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @Operation(
            summary = "Buscar inventario por ID",
            description = "Obtiene el detalle de un registro de inventario usando su identificador interno."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe inventario con el identificador indicado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(
            @Parameter(
                    description = "Identificador interno del inventario",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar inventario por SKU",
            description = "Obtiene el registro de inventario asociado a un SKU único."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado por SKU",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe inventario con el SKU indicado"
            )
    })
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Inventario> buscarPorSku(
            @Parameter(
                    description = "SKU único del producto",
                    required = true,
                    example = "NOTEBOOK-001"
            )
            @PathVariable String sku
    ) {
        return ResponseEntity.ok(inventarioService.buscarPorSku(sku));
    }

    @Operation(
            summary = "Buscar inventario por producto",
            description = "Obtiene el registro de inventario asociado a un producto específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado por producto",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe inventario para el producto indicado"
            )
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Inventario> buscarPorProductoId(
            @Parameter(
                    description = "Identificador del producto asociado",
                    required = true,
                    example = "100"
            )
            @PathVariable Long productoId
    ) {
        return ResponseEntity.ok(inventarioService.buscarPorProductoId(productoId));
    }

    @Operation(
            summary = "Crear inventario",
            description = "Crea un registro de inventario para un producto. Las cantidades no pueden ser negativas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Inventario creado correctamente",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida o campos obligatorios ausentes"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El SKU ya se encuentra registrado"
            )
    })
    @PostMapping
    public ResponseEntity<Inventario> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos necesarios para crear el inventario de un producto",
                    content = @Content(
                            schema = @Schema(implementation = Inventario.class),
                            examples = @ExampleObject(
                                    name = "Inventario inicial",
                                    value = """
                                            {
                                              "productoId": 100,
                                              "sku": "NOTEBOOK-001",
                                              "cantidadDisponible": 20,
                                              "cantidadReservada": 0,
                                              "ubicacion": "Bodega A-01"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody Inventario inventario
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crear(inventario));
    }

    @Operation(
            summary = "Actualizar inventario",
            description = "Actualiza los datos de un registro de inventario existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida o cantidades negativas"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El SKU ya está asociado a otro inventario"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(
            @Parameter(
                    description = "Identificador del inventario a actualizar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos actualizados del inventario",
                    content = @Content(
                            schema = @Schema(implementation = Inventario.class),
                            examples = @ExampleObject(
                                    name = "Inventario actualizado",
                                    value = """
                                            {
                                              "productoId": 100,
                                              "sku": "NOTEBOOK-001",
                                              "cantidadDisponible": 25,
                                              "cantidadReservada": 3,
                                              "ubicacion": "Bodega A-02"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody Inventario inventario
    ) {
        return ResponseEntity.ok(inventarioService.actualizar(id, inventario));
    }

    @Operation(
            summary = "Reservar stock",
            description = "Aumenta la cantidad reservada cuando existe stock libre suficiente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock reservado correctamente",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cantidad inválida o stock libre insuficiente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado"
            )
    })
    @PatchMapping("/{id}/reservar")
    public ResponseEntity<Inventario> reservarStock(
            @Parameter(
                    description = "Identificador del inventario",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Unidades que se desean reservar",
                    required = true,
                    example = "2"
            )
            @RequestParam Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.reservarStock(id, cantidad));
    }

    @Operation(
            summary = "Liberar reserva de stock",
            description = "Disminuye la cantidad reservada de un inventario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva liberada correctamente",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cantidad inválida o mayor a la reserva actual"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado"
            )
    })
    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Inventario> liberarReserva(
            @Parameter(
                    description = "Identificador del inventario",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Unidades de reserva que se desean liberar",
                    required = true,
                    example = "1"
            )
            @RequestParam Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.liberarReserva(id, cantidad));
    }

    @Operation(
            summary = "Descontar stock",
            description = "Reduce el stock disponible luego de una venta o confirmación de pedido."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock descontado correctamente",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cantidad inválida o stock insuficiente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado"
            )
    })
    @PatchMapping("/{id}/descontar")
    public ResponseEntity<Inventario> descontarStock(
            @Parameter(
                    description = "Identificador del inventario",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Unidades que se desean descontar",
                    required = true,
                    example = "1"
            )
            @RequestParam Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.descontarStock(id, cantidad));
    }

    @Operation(
            summary = "Reponer stock",
            description = "Aumenta el stock disponible de un producto."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock repuesto correctamente",
                    content = @Content(schema = @Schema(implementation = Inventario.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cantidad inválida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado"
            )
    })
    @PatchMapping("/{id}/reponer")
    public ResponseEntity<Inventario> reponerStock(
            @Parameter(
                    description = "Identificador del inventario",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Unidades que se desean agregar",
                    required = true,
                    example = "10"
            )
            @RequestParam Integer cantidad
    ) {
        return ResponseEntity.ok(inventarioService.reponerStock(id, cantidad));
    }

    @Operation(
            summary = "Desactivar inventario",
            description = "Realiza una desactivación lógica del registro de inventario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Inventario desactivado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @Parameter(
                    description = "Identificador del inventario a desactivar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        inventarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
