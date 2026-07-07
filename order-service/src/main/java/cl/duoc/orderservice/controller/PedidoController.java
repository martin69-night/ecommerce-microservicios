package cl.duoc.orderservice.controller;

import cl.duoc.orderservice.model.Pedido;
import cl.duoc.orderservice.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
        name = "Pedidos",
        description = "Operaciones para crear, consultar, actualizar y cancelar pedidos"
)
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(
            summary = "Listar pedidos activos",
            description = "Obtiene todos los pedidos que se encuentran activos en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos activos obtenidos correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Pedido.class)
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Pedido>> listarActivos() {
        return ResponseEntity.ok(pedidoService.listarActivos());
    }

    @Operation(
            summary = "Listar pedidos por usuario",
            description = "Obtiene los pedidos activos asociados a un usuario específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos del usuario obtenidos correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Pedido.class)
                            )
                    )
            )
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> listarPorUsuario(
            @Parameter(
                    description = "Identificador del usuario propietario de los pedidos",
                    required = true,
                    example = "1"
            )
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuarioId));
    }

    @Operation(
            summary = "Listar pedidos por estado",
            description = "Obtiene los pedidos activos de un estado específico. El estado se convierte automáticamente a mayúsculas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos filtrados por estado correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Pedido.class)
                            )
                    )
            )
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> listarPorEstado(
            @Parameter(
                    description = "Estado por el cual se filtrarán los pedidos",
                    required = true,
                    example = "CREADO"
            )
            @PathVariable String estado
    ) {
        return ResponseEntity.ok(pedidoService.listarPorEstado(estado.toUpperCase()));
    }

    @Operation(
            summary = "Buscar pedido por ID",
            description = "Obtiene el detalle de un pedido mediante su identificador interno."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado",
                    content = @Content(schema = @Schema(implementation = Pedido.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un pedido con el identificador indicado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(
            @Parameter(
                    description = "Identificador interno del pedido",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @Operation(
            summary = "Crear pedido y reservar stock",
            description = """
                    Crea un pedido nuevo y reserva el stock solicitado.

                    Order Service consulta Inventory Service mediante el SKU recibido.
                    Para crear el pedido, el inventario debe existir, estar activo y tener
                    stock libre suficiente. El pedido se registra con estado CREADO.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido creado y stock reservado correctamente",
                    content = @Content(schema = @Schema(implementation = Pedido.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos, inventario inactivo, SKU sin inventario o stock insuficiente"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "No fue posible comunicarse con Inventory Service"
            )
    })
    @PostMapping
    public ResponseEntity<Pedido> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos necesarios para crear un pedido y reservar stock",
                    content = @Content(
                            schema = @Schema(implementation = Pedido.class),
                            examples = @ExampleObject(
                                    name = "Pedido nuevo",
                                    value = """
                                            {
                                              "usuarioId": 1,
                                              "productoId": 100,
                                              "sku": "NOTEBOOK-001",
                                              "nombreProducto": "Notebook Gamer 15 pulgadas",
                                              "precioUnitario": 799990.00,
                                              "cantidad": 2
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody Pedido pedido
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(pedido));
    }

    @Operation(
            summary = "Cambiar estado de pedido",
            description = "Actualiza el estado de un pedido existente. El valor recibido se almacena en mayúsculas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado del pedido actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = Pedido.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El estado está vacío o es inválido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado"
            )
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(
            @Parameter(
                    description = "Identificador del pedido a actualizar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Nuevo estado del pedido",
                    required = true,
                    example = "PAGADO"
            )
            @RequestParam String estado
    ) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, estado));
    }

    @Operation(
            summary = "Cancelar pedido",
            description = """
                    Cambia el estado del pedido a CANCELADO y realiza una desactivación lógica.

                    Un pedido que se encuentre en estado PAGADO no puede cancelarse.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido cancelado correctamente",
                    content = @Content(schema = @Schema(implementation = Pedido.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No se puede cancelar un pedido pagado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado"
            )
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelar(
            @Parameter(
                    description = "Identificador del pedido a cancelar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }
}
