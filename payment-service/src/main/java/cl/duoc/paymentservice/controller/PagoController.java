package cl.duoc.paymentservice.controller;

import cl.duoc.paymentservice.model.Pago;
import cl.duoc.paymentservice.service.PagoService;
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
        name = "Pagos",
        description = "Operaciones para crear, consultar y cambiar el estado de pagos"
)
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Operation(
            summary = "Listar pagos activos",
            description = "Obtiene todos los pagos que se encuentran activos en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagos activos obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<Pago>> listarActivos() {
        return ResponseEntity.ok(pagoService.listarActivos());
    }

    @Operation(
            summary = "Listar pagos por usuario",
            description = "Obtiene los pagos activos asociados a un usuario específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagos del usuario obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Identificador de usuario inválido"
            )
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> listarPorUsuario(
            @Parameter(
                    description = "Identificador del usuario",
                    required = true,
                    example = "1"
            )
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(pagoService.listarPorUsuario(usuarioId));
    }

    @Operation(
            summary = "Listar pagos por estado",
            description = "Obtiene los pagos activos filtrados por estado. Ejemplos: PENDIENTE, APROBADO, RECHAZADO o ANULADO."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagos filtrados obtenidos correctamente",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            )
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> listarPorEstado(
            @Parameter(
                    description = "Estado del pago",
                    required = true,
                    example = "PENDIENTE"
            )
            @PathVariable String estado
    ) {
        return ResponseEntity.ok(pagoService.listarPorEstado(estado));
    }

    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene el detalle de un pago usando su identificador interno."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago encontrado",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un pago con el identificador indicado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(
            @Parameter(
                    description = "Identificador interno del pago",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar pago por pedido",
            description = "Obtiene el pago asociado a un pedido específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago asociado al pedido encontrado",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe un pago asociado al pedido indicado"
            )
    })
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pago> buscarPorPedidoId(
            @Parameter(
                    description = "Identificador del pedido asociado",
                    required = true,
                    example = "100"
            )
            @PathVariable Long pedidoId
    ) {
        return ResponseEntity.ok(pagoService.buscarPorPedidoId(pedidoId));
    }

    @Operation(
            summary = "Crear pago",
            description = "Registra un nuevo pago. El servicio asigna el estado PENDIENTE cuando no se informa uno."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pago creado correctamente",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida o campos obligatorios ausentes"
            )
    })
    @PostMapping
    public ResponseEntity<Pago> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos necesarios para registrar un pago",
                    content = @Content(
                            schema = @Schema(implementation = Pago.class),
                            examples = @ExampleObject(
                                    name = "Pago pendiente",
                                    value = """
                                            {
                                              "pedidoId": 100,
                                              "usuarioId": 1,
                                              "monto": 29990.00,
                                              "metodoPago": "TARJETA_CREDITO"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody Pago pago
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crear(pago));
    }

    @Operation(
            summary = "Aprobar pago",
            description = "Cambia el estado de un pago a APROBADO cuando la transición es válida."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago aprobado correctamente",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El pago no puede aprobarse desde su estado actual"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado"
            )
    })
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<Pago> aprobar(
            @Parameter(
                    description = "Identificador del pago a aprobar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pagoService.aprobar(id));
    }

    @Operation(
            summary = "Rechazar pago",
            description = "Cambia el estado de un pago a RECHAZADO cuando la transición es válida."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago rechazado correctamente",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El pago no puede rechazarse desde su estado actual"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado"
            )
    })
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<Pago> rechazar(
            @Parameter(
                    description = "Identificador del pago a rechazar",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pagoService.rechazar(id));
    }

    @Operation(
            summary = "Anular pago",
            description = "Cambia el estado de un pago a ANULADO cuando la transición es válida."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago anulado correctamente",
                    content = @Content(schema = @Schema(implementation = Pago.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El pago no puede anularse desde su estado actual"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pago no encontrado"
            )
    })
    @PatchMapping("/{id}/anular")
    public ResponseEntity<Pago> anular(
            @Parameter(
                    description = "Identificador del pago a anular",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pagoService.anular(id));
    }
}
